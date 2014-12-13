package edu.fudan.se.crowdservice.jade;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.util.Logger;

/**
 * Created by Dawnwords on 2014/12/13.
 */
public class TemplateExecutingAgent extends Agent {
    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    public TemplateExecutingAgent() {
        super();
        setEnabledO2ACommunication(true, 0);
    }

    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                //TODO deal with template
                String name = (String) getAgent().getO2AObject();
                if (name != null) {
                    logger.log(Logger.INFO, "getO2AObject:" + name);
                } else {
                    block();
                    logger.log(Logger.INFO, "block");
                }
            }

        });
    }


}
