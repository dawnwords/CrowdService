package edu.fudan.se.crowdservice.jade;

import android.os.Binder;
import jade.android.RuntimeCallback;
import jade.wrapper.AgentController;

/**
 * Created by Dawnwords on 2014/12/13.
 */
public class AgentManager extends Binder {
    private JADEService service;

    AgentManager(JADEService service) {
        this.service = service;
    }

    public void startAgent(String agentName, String agentClassName, RuntimeCallback<AgentController> callback) {
        service.startAgent(agentName, agentClassName, callback);
    }
}
