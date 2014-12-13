package edu.fudan.se.crowdservice.jade;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import edu.fudan.se.crowdservice.util.ResultHolder;
import jade.android.MicroRuntimeService;
import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;
import jade.core.MicroRuntime;
import jade.util.Logger;
import jade.util.leap.Properties;
import jade.wrapper.ControllerException;

import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/13.
 */
public class JADEService extends Service {
    private Logger logger;
    private MicroRuntimeServiceBinder microRuntimeServiceBinder;
    private ServiceConnection serviceConnection;
    private String agentName;
    private ResultHolder<Binder> binderHolder;


    public static class AgentBinder extends Binder {
        private TemplateExecutingAgent agent;

        public TemplateExecutingAgent getAgent() {
            return agent;
        }

        private void setAgent(TemplateExecutingAgent agent) {
            this.agent = agent;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger = Logger.getJADELogger(this.getClass().getName());
        binderHolder = new ResultHolder<>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        agentName = ((JADEServiceIntent) intent).getAgentName();
        bindMicroRuntimeService();
        return binderHolder.get();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        unbindService(serviceConnection);
        return super.onUnbind(intent);
    }

    private void bindMicroRuntimeService() {
        serviceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                microRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
                logger.log(Level.INFO, "Gateway successfully bound to MicroRuntimeService");
                startContainer();
            }

            public void onServiceDisconnected(ComponentName className) {
                microRuntimeServiceBinder = null;
                logger.log(Level.INFO, "Gateway unbound from MicroRuntimeService");
            }
        };
        logger.log(Level.INFO, "Binding Gateway to MicroRuntimeService...");
        Intent intent = new Intent(getApplicationContext(), MicroRuntimeService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void startContainer() {
        if (!MicroRuntime.isRunning()) {
            Properties profile = JADEProperties.getInstance();
            microRuntimeServiceBinder.startAgentContainer(profile,
                    new RuntimeCallback<Void>() {
                        @Override
                        public void onSuccess(Void ignore) {
                            logger.log(Level.INFO, "Successfully start of the container...");
                            startAgent();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            fail("Failed to start the container...");
                        }
                    });
        } else {
            startAgent();
        }
    }

    private void startAgent() {
        final String agentClassName = TemplateExecutingAgent.class.getName();
        microRuntimeServiceBinder.startAgent(agentName,
                agentClassName,
                new Object[]{getApplicationContext()},
                new RuntimeCallback<Void>() {
                    @Override
                    public void onSuccess(Void thisIsNull) {
                        logger.log(Level.INFO, "Successfully start of the " + agentClassName + "...");
                        setAgent();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        fail("Failed to start the " + agentClassName + "...");
                    }

                });
    }

    private void setAgent() {
        try {
            AgentBinder binder = new AgentBinder();
            binder.setAgent((TemplateExecutingAgent) MicroRuntime.getAgent(agentName));
            binderHolder.set(binder);
        } catch (ControllerException e) {
            fail(e.getMessage());
        }
    }

    private void fail(String reason) {
        logger.log(Level.SEVERE, reason);
        binderHolder.set(null);
    }
}
