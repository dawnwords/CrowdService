package edu.fudan.se.crowdservice.jade;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import jade.android.MicroRuntimeService;
import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;
import jade.core.MicroRuntime;
import jade.util.Logger;
import jade.util.leap.Properties;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/13.
 */
public class JADEService extends Service {
    private Logger logger;
    private MicroRuntimeServiceBinder microRuntimeServiceBinder;
    private boolean gatewayReady, containerReady;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            microRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
            info("Gateway successfully bound to MicroRuntimeService");
            gatewayReady = true;
            startContainer();
        }

        public void onServiceDisconnected(ComponentName className) {
            microRuntimeServiceBinder = null;
            info("Gateway unbound from MicroRuntimeService");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        logger = Logger.getJADELogger(this.getClass().getName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        bindMicroRuntimeService();
        return new AgentManager(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        unbindService(serviceConnection);
        return super.onUnbind(intent);
    }

    private void bindMicroRuntimeService() {
        logger.log(Level.INFO, "Binding Gateway to MicroRuntimeService...");
        Intent intent = new Intent(getApplicationContext(), MicroRuntimeService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void startContainer() {
        logger.log(Level.INFO, "Starting JADE container...");
        if (!MicroRuntime.isRunning()) {
            Properties profile = JADEProperties.getInstance();
            microRuntimeServiceBinder.startAgentContainer(profile,
                    new RuntimeCallback<Void>() {
                        @Override
                        public void onSuccess(Void ignore) {
                            containerReady = true;
                            info("Successfully start of the container...");
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            err("Failed to start the container...");
                        }
                    });
        } else {
            containerReady = true;
        }
    }

    void startAgent(final String agentName, final String agentClassName, final RuntimeCallback<AgentController> callback) {
        if (!(gatewayReady && containerReady)) {
            err("JADE is not ready...");
            return;
        }
        info("Starting " + agentClassName + "...");
        microRuntimeServiceBinder.startAgent(agentName, agentClassName, new Object[]{getApplicationContext()},
                new RuntimeCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        info("Successfully start of the " + agentClassName + "...");
                        try {
                            callback.onSuccess(MicroRuntime.getAgent(agentName));
                        } catch (ControllerException e) {
                            err("Failed to start the " + agentClassName + ":" + e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        err("Failed to start the " + agentClassName + "...");
                    }

                });
    }

    private void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    private void err(String reason) {
        logger.log(Level.SEVERE, reason);
    }
}
