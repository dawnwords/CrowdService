package edu.fudan.se.crowdservice.jade;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.util.Logger;

/**
 * Created by Dawnwords on 2014/12/13.
 */
public class DaemonAgent extends Agent {
    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    public DaemonAgent() {
        super();
        setEnabledO2ACommunication(true, 0);
    }

    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                info("thread name:"+Thread.currentThread().getName());
                //TODO deal with template
                String name = (String) getAgent().getO2AObject();
                if (name != null) {
                    info("getO2AObject:" + name);
                } else {
                    block();
                    info("block");
                }
            }

        });
    }

    private void info(String msg) {
        logger.log(Logger.INFO, msg);
    }


}
