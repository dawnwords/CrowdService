package edu.fudan.se.crowdservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import edu.fudan.se.crowdservice.core.Template;
import edu.fudan.se.crowdservice.felix.FelixService;
import edu.fudan.se.crowdservice.felix.TemplateManager;
import edu.fudan.se.crowdservice.jade.AgentManager;
import edu.fudan.se.crowdservice.jade.DaemonAgent;
import edu.fudan.se.crowdservice.jade.JADEService;
import edu.fudan.se.crowdservice.jade.TemplateExecutor;
import edu.fudan.se.crowdservice.view.TemplateListView;
import jade.android.RuntimeCallback;
import jade.util.Logger;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import org.osgi.service.obr.Requirement;
import org.osgi.service.obr.Resource;

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
    private Logger logger = Logger.getJADELogger(this.getClass().getName());


    private TemplateListView templateListView;
    private Resource[] templateResources;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bindService(new Intent(getApplicationContext(), JADEService.class), jadeConnection, BIND_AUTO_CREATE);
        bindService(new Intent(getApplicationContext(), FelixService.class), felixConnection, BIND_AUTO_CREATE);

        templateListView = (TemplateListView) findViewById(R.id.template_list);
        templateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                templateManager.resolveTemplate(templateResources[i], new TemplateManager.OnTemplateResolvedListener() {
                    @Override
                    public void onTemplateResolved(Template template) {
                        try {
                            TemplateExecutor executor = agentController.getO2AInterface(TemplateExecutor.class);
                            if (executor != null) {
                                executor.executeTemplate(template);
                            }
                        } catch (StaleProxyException e) {
                            showMessage(e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Requirement[] unsatisfiedRequirements) {
                        String output = "";
                        for (Requirement requirement : unsatisfiedRequirements) {
                            output += String.format("%s:%s\n", requirement.getName(), requirement.getComment());
                        }
                        showMessage(output);
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        unbindService(felixConnection);
        unbindService(jadeConnection);
        super.onDestroy();
    }

    public void createAgent(final View v) {
        agentManager.startAgent("echo-agent", DaemonAgent.class.getName(), new RuntimeCallback<AgentController>() {
            @Override
            public void onSuccess(AgentController agentController) {
                MainActivity.this.agentController = agentController;
            }

            @Override
            public void onFailure(Throwable throwable) {
                showMessage("Unable to start daemon agent!");
            }
        });
    }


    public void getAvailableTemplate(View v) {
        templateManager.getAvailableTemplateResource(new TemplateManager.OnResourcesReceivedListener() {
            @Override
            public void onResourcesReceived(Resource[] resources) {
                templateResources = resources;
                templateListView.setTemplateResource(resources);
            }

            @Override
            public void onFailure() {
                showMessage("Unable to fetch template list!");
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
