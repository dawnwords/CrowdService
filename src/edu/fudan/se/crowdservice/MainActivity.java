package edu.fudan.se.crowdservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import edu.fudan.se.crowdservice.felix.FelixService;
import edu.fudan.se.crowdservice.jade.AgentManager;
import edu.fudan.se.crowdservice.jade.DaemonAgent;
import edu.fudan.se.crowdservice.jade.JADEService;
import jade.android.RuntimeCallback;
import jade.util.Logger;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.logging.Level;

public class MainActivity extends Activity {
    private AgentManager agent;
    private AgentController agentController;
    private ServiceConnection jadeConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            agent = (AgentManager) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            agent = null;
        }
    };
    private ServiceConnection felixConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bindService(new Intent(getApplicationContext(), JADEService.class), jadeConnection, BIND_AUTO_CREATE);
        bindService(new Intent(getApplicationContext(), FelixService.class), felixConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(felixConnection);
        unbindService(jadeConnection);
        super.onDestroy();
    }

    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    public void createAgent(final View v) {
        agent.startAgent("echo-agent", DaemonAgent.class.getName(), new RuntimeCallback<AgentController>() {
            @Override
            public void onSuccess(AgentController agentController) {
                MainActivity.this.agentController = agentController;
                sendO2AObject(v);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public void sendO2AObject(View v) {
        try {
            agentController.putO2AObject("hello", false);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, "putO2AObject");
    }
}
