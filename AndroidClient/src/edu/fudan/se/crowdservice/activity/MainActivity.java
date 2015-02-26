package edu.fudan.se.crowdservice.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.core.Template;
import edu.fudan.se.crowdservice.data.ConsumerSession;
import edu.fudan.se.crowdservice.felix.FelixService;
import edu.fudan.se.crowdservice.felix.TemplateManager;
import edu.fudan.se.crowdservice.fragment.BaseFragment;
import edu.fudan.se.crowdservice.fragment.ConsumerFragment;
import edu.fudan.se.crowdservice.fragment.NavigationFragment;
import edu.fudan.se.crowdservice.fragment.NavigationFragment.NavigationDrawerCallbacks;
import edu.fudan.se.crowdservice.fragment.WorkerFragment;
import edu.fudan.se.crowdservice.jade.AgentManager;
import edu.fudan.se.crowdservice.jade.JADEService;
import edu.fudan.se.crowdservice.jade.agent.uimessage.*;
import edu.fudan.se.crowdservice.wrapper.DelegateWrapper;
import edu.fudan.se.crowdservice.wrapper.RefuseWrapper;
import edu.fudan.se.crowdservice.wrapper.RequestWrapper;

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks {

    private static final String[] FRAGMENT_TAG = {"Consumer", "Worker"};
    private NavigationFragment navigationFragment;
    private Fragment lastFragment;
    private ServiceConnection felixConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TemplateManager tm = (TemplateManager) iBinder;
            navigationFragment.setTemplateManager(tm);
            navigationFragment.setTemplateSelectCallBack(new NavigationFragment.TemplateSelectCallbacks() {
                @Override
                public void onTemplateSelected(Template template) {
                    ConsumerFragment fragment = (ConsumerFragment) getSupportFragmentManager().findFragmentByTag("Consumer");
                    if (fragment != null) {
                        fragment.addTemplate(template);
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            navigationFragment.setTemplateManager(null);
        }

    };
    private Handler uiMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            UIMessage message = new UIMessage(msg);
            switch (message.what()) {
                case RefuseMessage.WHAT:
                    refuseMessage(message);
                    break;
                case RequestMessage.WHAT:
                    requestMessage(message);
                    break;
                case DelegateMessage.WHAT:
                    delegateMessage(message);
                    break;
                case ConsumerSessionMessage.WHAT:
                    consumerSessionMessage(message);
                    break;
            }
        }
    };

    private ServiceConnection jadeConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AgentManager am = (AgentManager) iBinder;
            am.registerHandler(uiMessageHandler);
            setAgent(am);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            setAgent(null);
        }

        private void setAgent(AgentManager agent) {
            FragmentManager manager = getSupportFragmentManager();
            for (String fragmentTag : FRAGMENT_TAG) {
                ((BaseFragment) manager.findFragmentByTag(fragmentTag)).setAgent(agent);
            }
        }
    };

    private void delegateMessage(UIMessage message) {
        DelegateWrapper delegate = (DelegateWrapper) message.getValue();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TaskSubmitActivity.UI_MODEL, delegate);
        Intent intent = new Intent(this, TaskSubmitActivity.class);
        intent.putExtra(TaskSubmitActivity.EXTRA_BUNDLE, bundle);
        startActivity(intent);
    }

    private void requestMessage(UIMessage message) {
        RequestWrapper request = (RequestWrapper) message.getValue();
        ((WorkerFragment) getSupportFragmentManager().findFragmentByTag("Worker")).addRequest(request);
        toast("You receive a request:" + request.taskId);
    }

    private void refuseMessage(UIMessage message) {
        RefuseWrapper refuse = (RefuseWrapper) message.getValue();
        toast("Your offer for task" + refuse.taskId + " is refused:" + refuse.reason);
    }

    private void consumerSessionMessage(UIMessage message) {
        switch (ConsumerSession.MessageType.valueOf(message.what())) {
            case CONSUMER_INPUT:
                //TODO addMessage
                break;
            case SERVICE_START:
                break;
            case SERVICE_STOP:
                break;
            case SERVICE_EXCEPTION:
                break;
            case REQUEST_INPUT:
                break;
            case REQUEST_CONFIRM:
                break;
            case REQUEST_CHOOSE:
                break;
            case SHOW_MESSAGE:
                break;
            case TEMPLATE_STOP:
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        bindService(new Intent(getApplicationContext(), JADEService.class), jadeConnection, BIND_AUTO_CREATE);
        bindService(new Intent(getApplicationContext(), FelixService.class), felixConnection, BIND_AUTO_CREATE);
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        initFragments();
    }

    private void initFragments() {
        navigationFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        navigationFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        for (String fragmentTitle : FRAGMENT_TAG) {
            onNavigationDrawerItemSelected(fragmentTitle);
        }
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

    @Override
    public void onNavigationDrawerItemSelected(String title) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment currentFragment = manager.findFragmentByTag(title);
        if (currentFragment == null) {
            currentFragment = createFragment(title);
            transaction.add(R.id.content_frame, currentFragment, title);
        }
        if (lastFragment != null) {
            transaction.hide(lastFragment);
        }
        if (currentFragment.isDetached()) {
            transaction.attach(currentFragment);
        }
        transaction.show(currentFragment);
        lastFragment = currentFragment;
        transaction.commit();
    }

    private Fragment createFragment(String title) {
        Fragment result = null;
        try {
            String fragmentClassName = String.format("edu.fudan.se.crowdservice.fragment.%sFragment", title);
            result = (Fragment) Class.forName(fragmentClassName).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
