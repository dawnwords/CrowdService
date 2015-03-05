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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.Toast;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.core.Template;
import edu.fudan.se.crowdservice.data.ConsumerSession;
import edu.fudan.se.crowdservice.felix.FelixService;
import edu.fudan.se.crowdservice.felix.TemplateManager;
import edu.fudan.se.crowdservice.fragment.*;
import edu.fudan.se.crowdservice.fragment.NavigationFragment.NavigationDrawerCallbacks;
import edu.fudan.se.crowdservice.jade.AgentManager;
import edu.fudan.se.crowdservice.jade.JADEService;
import edu.fudan.se.crowdservice.jade.agent.uimessage.*;
import edu.fudan.se.crowdservice.wrapper.Wrapper;

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks {

    private static final String[] FRAGMENT_TAG = {"TaskSubmit", "ConsumerSession", "Consumer", "Worker"};
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
                    ConsumerFragment fragment = (ConsumerFragment) getFragmentByTag("Consumer");
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
                case TaskMessage.WHAT:
                    taskMessage(message);
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
            for (String fragmentTag : FRAGMENT_TAG) {
                ((BaseFragment) getFragmentByTag(fragmentTag)).setAgent(agent);
            }
        }
    };

    private void taskMessage(UIMessage message) {
        Wrapper messageWrapper = (Wrapper) message.getValue();
        ((WorkerFragment) getSupportFragmentManager().findFragmentByTag("Worker")).addMessageWrapper(messageWrapper);
    }

    private void consumerSessionMessage(UIMessage message) {
        ConsumerSession.Message csm = (ConsumerSession.Message) message.getValue();
        ((ConsumerFragment) getFragmentByTag("Consumer")).addConsumerSessionMessage(csm);
        ConsumerSessionFragment consumerSession = (ConsumerSessionFragment) getFragmentByTag("ConsumerSession");
        if (lastFragment == consumerSession) {
            consumerSession.updateConsumerSessionMessage();
        }
    }

    private Fragment getFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
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

        navigationFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        navigationFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onDestroy() {
        unbindService(felixConnection);
        unbindService(jadeConnection);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isCurrentFragment("ConsumerSession") && !isCurrentFragment("TaskSubmit")) {
            getMenuInflater().inflate(R.menu.main, menu);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    private boolean isCurrentFragment(String tag) {
        return lastFragment != null && tag != null && tag.equals(lastFragment.getTag());
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNavigationDrawerItemSelected(String title) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment currentFragment = getFragmentByTag(title);
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
