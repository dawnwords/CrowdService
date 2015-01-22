package edu.fudan.se.crowdservice.jade.agent;

import android.location.Location;
import edu.fudan.se.crowdservice.bean.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.core.Template;
import edu.fudan.se.crowdservice.jade.agent.behaviour.TemplateExecutingBehaviour;
import jade.core.Agent;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.util.Logger;

import java.util.ArrayList;

/**
 * Created by Dawnwords on 2014/12/13.
 */
public class DaemonAgent extends Agent implements AgentInterface {
    private Logger logger = Logger.getJADELogger(this.getClass().getName());
    private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

    private Location location;

    @Override
    protected void setup() {
        info("Register TemplateExecutor O2AInterface...");
        registerO2AInterface(AgentInterface.class, this);
    }

    private void info(String msg) {
        logger.log(Logger.INFO, msg);
    }


    @Override
    public void sendCapacity(String capacity) {

    }

    @Override
    public void sendCustomLocation(Location myLocation) {
        this.location = myLocation;
    }

    @Override
    public void sendResponse(ArrayList<KeyValueHolder> response) {

    }

    @Override
    public void executeTemplate(Template template) {
        addBehaviour(tbf.wrap(new TemplateExecutingBehaviour(template)));
    }

}
