package edu.fudan.se.crowdservice.jade;

import android.app.Service;
import android.content.*;
import android.os.IBinder;
import android.util.Log;
import edu.fudan.se.crowdservice.core.SavedProperty;
import edu.fudan.se.crowdservice.jade.agent.AgentInterface;
import edu.fudan.se.crowdservice.jade.agent.DaemonAgent;
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
    private SharedPreferences setting;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            microRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
            info("Gateway successfully bound to MicroRuntimeService");
            startContainer();
        }

        public void onServiceDisconnected(ComponentName className) {
            microRuntimeServiceBinder = null;
            info("Gateway unbound from MicroRuntimeService");
        }
    };
    private AgentManager agentManager;

    @Override
    public void onCreate() {
        super.onCreate();
        logger = Logger.getJADELogger(this.getClass().getName());
        setting = getSharedPreferences(SavedProperty.CROWD_SERVICE, 0);

        bindMicroRuntimeService();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (agentManager == null) {
            agentManager = new AgentManager();
        }
        Log.i("onBind", "agent manager:" + agentManager);
        return agentManager;
    }

    @Override
    public void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    private void bindMicroRuntimeService() {
        logger.log(Level.INFO, "Binding Gateway to MicroRuntimeService...");
        Intent intent = new Intent(getApplicationContext(), MicroRuntimeService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void startContainer() {
        logger.log(Level.INFO, "Starting JADE container...");
        if (!MicroRuntime.isRunning()) {
            String jadeIP = getTextStored(SavedProperty.JADE_IP);
            int jadePort = setting.getInt(SavedProperty.JADE_PORT, 1099);
            Properties profile = JADEProperties.getInstance(jadeIP, jadePort);
            microRuntimeServiceBinder.startAgentContainer(profile, new RuntimeCallback<Void>() {
                @Override
                public void onSuccess(Void ignore) {
                    startAgent();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    err("Failed to start the container...");
                }
            });
        }
    }

    private void startAgent() {
        final String agentName = getTextStored(SavedProperty.AGENT_NAME);
        final String agentClassName = DaemonAgent.class.getName();
        info("Starting " + agentClassName + "...");
        microRuntimeServiceBinder.startAgent(agentName, agentClassName, new Object[]{getApplicationContext()},
                new RuntimeCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        info("Successfully start of the " + agentClassName);
                        try {
                            AgentInterface agent = MicroRuntime.getAgent(agentName).getO2AInterface(AgentInterface.class);
                            info("agent interface=" + agent);
                            agentManager.setAgent(agent);
                            agent.setContext(JADEService.this);
                            agent.sendCapacity(getTextStored(SavedProperty.CAPACITY));
                        } catch (ControllerException e) {
                            onFailure(e);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        err("Failed to start the " + agentClassName + "...");
                        throwable.printStackTrace();
                        agentManager.setAgent(null);
                    }

                });
    }

    private void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    private void err(String reason) {
        logger.log(Level.SEVERE, reason);
    }

    private String getTextStored(String key) {
        return setting.getString(key, "");
    }
}
