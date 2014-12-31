package edu.fudan.se.crowdservice.jade;

import edu.fudan.se.crowdservice.core.Template;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.util.Logger;

/**
 * Created by Dawnwords on 2014/12/13.
 */
public class DaemonAgent extends Agent implements TemplateExecutor {
    private Logger logger = Logger.getJADELogger(this.getClass().getName());
    private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

    @Override
    protected void setup() {
        info("Register TemplateExecutor O2AInterface...");
        registerO2AInterface(TemplateExecutor.class, this);
    }

    private void info(String msg) {
        logger.log(Logger.INFO, msg);
    }

    @Override
    public void executeTemplate(final Template template) {
        addBehaviour(tbf.wrap(new OneShotBehaviour() {
            @Override
            public void action() {
                template.executeTemplate();
            }
        }));
    }
}
