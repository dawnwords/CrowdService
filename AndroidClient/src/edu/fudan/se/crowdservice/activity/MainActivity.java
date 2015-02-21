package edu.fudan.se.crowdservice.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.felix.FelixService;
import edu.fudan.se.crowdservice.felix.TemplateManager;
import edu.fudan.se.crowdservice.fragment.BaseFragment;
import edu.fudan.se.crowdservice.fragment.ConsumerFragment;
import edu.fudan.se.crowdservice.fragment.WorkerFragment;
import edu.fudan.se.crowdservice.jade.AgentManager;
import edu.fudan.se.crowdservice.jade.JADEService;
import edu.fudan.se.crowdservice.jade.agent.uimessage.DelegateMessage;
import edu.fudan.se.crowdservice.jade.agent.uimessage.RefuseMessage;
import edu.fudan.se.crowdservice.jade.agent.uimessage.RequestMessage;
import edu.fudan.se.crowdservice.jade.agent.uimessage.UIMessage;
import edu.fudan.se.crowdservice.wrapper.DelegateWrapper;
import edu.fudan.se.crowdservice.wrapper.RefuseWrapper;
import edu.fudan.se.crowdservice.wrapper.RequestWrapper;

public class MainActivity extends Activity {
    private ConsumerFragment consumerFragment;
    private WorkerFragment workerFragment;
    private FragmentManager fragmentManager;
    private Spinner switcher;
    private ServiceConnection felixConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TemplateManager tm = (TemplateManager) iBinder;
            consumerFragment.setTemplateManager(tm);

            consumerFragment.loadAvailableTemplates();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            consumerFragment.setTemplateManager(null);
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            UIMessage message = new UIMessage(msg);
            switch (message.what()) {
                case RefuseMessage.REFUSE:
                    RefuseWrapper refuse = (RefuseWrapper) message.getValue();
                    toast("Your offer for task" + refuse.taskId + " is refused:" + refuse.reason);
                    break;
                case RequestMessage.REQUEST:
                    RequestWrapper request = (RequestWrapper) message.getValue();
                    workerFragment.addRequest(request);
                    toast("You receive a request:" + request.taskId);
                    break;
                case DelegateMessage.DELEGATE:
                    DelegateWrapper delegate = (DelegateWrapper) message.getValue();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(TaskSubmitActivity.UI_MODEL, delegate);
                    Intent intent = new Intent(MainActivity.this, TaskSubmitActivity.class);
                    intent.putExtra(TaskSubmitActivity.EXTRA_BUNDLE, bundle);
                    startActivity(intent);
                    break;
            }
        }
    };
    private ServiceConnection jadeConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AgentManager am = (AgentManager) iBinder;
            am.registerHandler(handler);
            workerFragment.setAgent(am);
            consumerFragment.setAgent(am);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            workerFragment.setAgent(null);
            consumerFragment.setAgent(null);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments();
        initSwitcher();

        bindService(new Intent(getApplicationContext(), JADEService.class), jadeConnection, BIND_AUTO_CREATE);
        bindService(new Intent(getApplicationContext(), FelixService.class), felixConnection, BIND_AUTO_CREATE);
    }

    private void initSwitcher() {
        switcher = (Spinner) findViewById(R.id.switcher);
        switcher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("onItemSelected", "position = " + position);
                if (position == 0) {
                    replaceFragment(consumerFragment, workerFragment);
                } else {
                    replaceFragment(workerFragment, consumerFragment);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            private void replaceFragment(BaseFragment newFragment, BaseFragment oldFragment) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.hide(oldFragment);
                transaction.show(newFragment);
                transaction.commit();
            }
        });
        switcher.setSelection(0);
    }


    private void initFragments() {
        consumerFragment = new ConsumerFragment();
        workerFragment = new WorkerFragment();

        fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_content, consumerFragment);
        transaction.add(R.id.main_content, workerFragment);
        transaction.hide(workerFragment);
        transaction.commit();
    }


    @Override
    protected void onDestroy() {
        unbindService(felixConnection);
        unbindService(jadeConnection);
        super.onDestroy();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
