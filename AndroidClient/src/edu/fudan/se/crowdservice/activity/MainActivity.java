package edu.fudan.se.crowdservice.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.felix.FelixService;
import edu.fudan.se.crowdservice.felix.TemplateManager;
import edu.fudan.se.crowdservice.fragment.ConsumerFragment;
import edu.fudan.se.crowdservice.fragment.WorkerFragment;
import edu.fudan.se.crowdservice.jade.AgentManager;
import edu.fudan.se.crowdservice.jade.JADEService;
import jade.wrapper.AgentController;

public class MainActivity extends Activity {
    private AgentManager agentManager;
    private TemplateManager templateManager;
    private AgentController agentController;
    private ServiceConnection jadeConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            agentManager = (AgentManager) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            agentManager = null;
        }
    };
    private ServiceConnection felixConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            templateManager = (TemplateManager) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            templateManager = null;
        }
    };

//    private TemplateListView templateListView;
//    private Resource[] templateResources;

    private ConsumerFragment consumerFragment;
    private WorkerFragment workerFragment;
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bindService(new Intent(getApplicationContext(), JADEService.class), jadeConnection, BIND_AUTO_CREATE);
        bindService(new Intent(getApplicationContext(), FelixService.class), felixConnection, BIND_AUTO_CREATE);

        fragmentManager = getFragmentManager();
        consumerFragment(null);

//        templateListView = (TemplateListView) findViewById(R.id.template_list);
//        templateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                templateManager.resolveTemplate(templateResources[i], MainActivity.this,
//                        new TemplateManager.OnTemplateResolvedListener() {
//                            @Override
//                            public void onTemplateResolved(Template template) {
//                                try {
//                                    AgentInterface executor = agentController.getO2AInterface(AgentInterface.class);
//                                    if (executor != null) {
//                                        executor.executeTemplate(template);
//                                    }
//                                } catch (StaleProxyException e) {
//                                    showMessage(e.getMessage());
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Requirement[] unsatisfiedRequirements) {
//                                String output = "";
//                                for (Requirement requirement : unsatisfiedRequirements) {
//                                    output += String.format("%s:%s\n", requirement.getName(), requirement.getComment());
//                                }
//                                showMessage(output);
//                            }
//                        });
//                templateListView.setClickable(false);
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        unbindService(felixConnection);
        unbindService(jadeConnection);
        super.onDestroy();
    }

    public void workerFragment(View v) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        if (workerFragment == null) {
            workerFragment = new WorkerFragment();
            transaction.add(R.id.main_content, workerFragment);
        } else {
            transaction.show(workerFragment);
        }
        transaction.commit();
    }

    public void consumerFragment(View v) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        if (consumerFragment == null) {
            consumerFragment = new ConsumerFragment();
            transaction.add(R.id.main_content, consumerFragment);
        } else {
            transaction.show(consumerFragment);
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (consumerFragment != null) {
            transaction.hide(consumerFragment);
        }
        if (workerFragment != null) {
            transaction.hide(workerFragment);
        }
    }

//    public void getAvailableTemplate(View v) {
//        templateManager.getAvailableTemplateResource(new TemplateManager.OnResourcesReceivedListener() {
//            @Override
//            public void onResourcesReceived(Resource[] resources) {
//                templateResources = resources;
//                templateListView.setTemplateResource(resources);
//            }
//
//            @Override
//            public void onFailure() {
//                showMessage("Unable to fetch template list!");
//            }
//        });
//    }

//    private void showMessage(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
}
