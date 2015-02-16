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
import android.view.View;
import android.widget.Toast;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.data.GPSLocator;
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
    private GPSLocator locator;
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
            locator.enableGPS(MainActivity.this, am);
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
        setContentView(R.layout.main);

        locator = new GPSLocator();

        consumerFragment = new ConsumerFragment();
        workerFragment = new WorkerFragment();

        fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_content, consumerFragment);
        transaction.add(R.id.main_content, workerFragment);
        transaction.hide(workerFragment);
        transaction.commit();

        bindService(new Intent(getApplicationContext(), JADEService.class), jadeConnection, BIND_AUTO_CREATE);
        bindService(new Intent(getApplicationContext(), FelixService.class), felixConnection, BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        locator.disableGPS();
        unbindService(felixConnection);
        unbindService(jadeConnection);
        super.onDestroy();
    }

    public void workerFragment(View v) {
        replaceFragment(workerFragment, consumerFragment);
    }

    public void consumerFragment(View v) {
        replaceFragment(consumerFragment, workerFragment);
        consumerFragment.loadAvailableTemplates();
    }

    private void replaceFragment(BaseFragment newFragment, BaseFragment oldFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(oldFragment);
        transaction.show(newFragment);
        transaction.commit();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
