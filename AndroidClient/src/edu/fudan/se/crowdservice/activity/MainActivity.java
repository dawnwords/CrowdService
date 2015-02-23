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
import edu.fudan.se.crowdservice.felix.FelixService;
import edu.fudan.se.crowdservice.felix.TemplateManager;
import edu.fudan.se.crowdservice.fragment.BaseFragment;
import edu.fudan.se.crowdservice.fragment.ConsumerFragment;
import edu.fudan.se.crowdservice.fragment.NavigationFragment;
import edu.fudan.se.crowdservice.fragment.NavigationFragment.NavigationDrawerCallbacks;
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

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks {
    //    private ConsumerFragment consumerFragment;
//    private WorkerFragment workerFragment;
//    private FragmentManager fragmentManager;
//    private TemplateListView templateList;
    private static final String[] FRAGMENT_TAG = {"Consumer", "Worker"};
    private NavigationFragment navigationFragment;
    private Fragment lastFragment;
    private ServiceConnection felixConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TemplateManager tm = (TemplateManager) iBinder;
            setTemplateManager(tm);
//            TODO change to fragment
//            templateList.setTemplateResolveListener(new TemplateManager.OnTemplateResolvedListener() {
//                @Override
//                public void onTemplateResolved(Template template) {
//                    toast(template.getClass().getName());
//                }
//
//                @Override
//                public void onFailure(Requirement[] unsatisfiedRequirements) {
//                    String output = "";
//                    for (Requirement requirement : unsatisfiedRequirements) {
//                        output += String.format("%s:%s\n", requirement.getName(), requirement.getComment());
//                    }
//                    toast(output);
//                }
//            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            setTemplateManager(null);
        }

        private void setTemplateManager(TemplateManager tm) {
            ((ConsumerFragment) getSupportFragmentManager().findFragmentByTag("Consumer")).setTemplateManager(tm);
//            templateList.setTemplateManager(tm);
        }

    };
    private Handler uiMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            UIMessage message = new UIMessage(msg);
            switch (message.what()) {
                case RefuseMessage.REFUSE:
                    refuseMessage(message);
                    break;
                case RequestMessage.REQUEST:
                    requestMessage(message);
                    break;
                case DelegateMessage.DELEGATE:
                    delegateMessage(message);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        bindService(new Intent(getApplicationContext(), JADEService.class), jadeConnection, BIND_AUTO_CREATE);
        bindService(new Intent(getApplicationContext(), FelixService.class), felixConnection, BIND_AUTO_CREATE);
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
//        templateList = (TemplateListView) findViewById(R.id.template_list);
//
        initFragments();
//        initSwitcher();
    }

    //
//    private void initSwitcher() {
//        Spinner switcher = (Spinner) findViewById(R.id.switcher);
//        switcher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.i("onItemSelected", "position = " + position);
//                if (position == 0) {
//                    replaceFragment(consumerFragment, workerFragment);
//                } else {
//                    replaceFragment(workerFragment, consumerFragment);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//
//            private void replaceFragment(BaseFragment newFragment, BaseFragment oldFragment) {
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.hide(oldFragment);
//                transaction.show(newFragment);
//                transaction.commit();
//            }
//        });
//        switcher.setSelection(0);
//    }
//
    private void initFragments() {
//        consumerFragment = new ConsumerFragment();
//        workerFragment = new WorkerFragment();
//
//        fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .add(R.id.content_frame, consumerFragment)
//                .add(R.id.content_frame, workerFragment)
//                .hide(workerFragment).commit();
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

//    public void addTemplate(View v) {
//        templateList.setVisibility(View.VISIBLE);
//        templateList.loadAvailableTemplates();
//    }

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
