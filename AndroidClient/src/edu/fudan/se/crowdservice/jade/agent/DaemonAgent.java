package edu.fudan.se.crowdservice.jade.agent;

import android.location.Location;
import android.os.Handler;
import edu.fudan.se.crowdservice.core.Template;
import edu.fudan.se.crowdservice.jade.agent.behaviour.SendCapacityBehaviour;
import edu.fudan.se.crowdservice.jade.agent.behaviour.SendResponseBehaviour;
import edu.fudan.se.crowdservice.jade.agent.behaviour.TemplateExecutingBehaviour;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import jade.core.Agent;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Dawnwords on 2014/12/13.
 */
public class DaemonAgent extends Agent implements AgentInterface {
    private static final long HEARTBEAT_FREQUENCY = 1000;
    private Logger logger = Logger.getJADELogger(this.getClass().getName());
    private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

    private Location location;
    private Handler handler;

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
        addBehaviour(new SendCapacityBehaviour(capacity));
        addBehaviour(new TickerBehaviour(this, HEARTBEAT_FREQUENCY) {
            @Override
            protected void onTick() {
                sendHeartbeat();
            }
        });
    }

    private synchronized void sendHeartbeat() {
        if (location != null) {
            ACLMessage aclMsg = new ACLMessage(ACLMessage.INFORM);
            aclMsg.setConversationId(ConversationType.HEARTBEAT.name());
            aclMsg.addReceiver(ConversationType.HEARTBEAT.target());
            try {
                aclMsg.setContentObject(location.getLongitude() + ":" + location.getLatitude());
                send(aclMsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void registerHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public synchronized void setLocation(Location myLocation) {
        this.location = myLocation;
    }

    @Override
    public void sendResponse(long taskid, ArrayList<KeyValueHolder> response) {
        addBehaviour(new SendResponseBehaviour(taskid, response));
    }

    @Override
    public void executeTemplate(Template template) {
        addBehaviour(tbf.wrap(new TemplateExecutingBehaviour(template)));
    }

}
