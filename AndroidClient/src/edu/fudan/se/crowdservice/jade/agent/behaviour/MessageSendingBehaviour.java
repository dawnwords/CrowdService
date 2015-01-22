package edu.fudan.se.crowdservice.jade.agent.behaviour;

import edu.fudan.se.crowdservice.jade.agent.ConversationType;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;

/**
 * Created by Jiahuan on 2015/1/21.
 */
public abstract class MessageSendingBehaviour<T extends Serializable> extends OneShotBehaviour {
    private Logger logger = Logger.getJADELogger(this.getClass().getName());
    private ConversationType conversationType;
    private T content;

    public MessageSendingBehaviour(ConversationType conversationType, T content) {
        this.conversationType = conversationType;
        this.content = content;
    }

    @Override
    public void action() {
        AID targetAgent = conversationType.target();
        ACLMessage aclMsg = new ACLMessage(ACLMessage.INFORM);
        aclMsg.setConversationId(conversationType.name());
        aclMsg.addReceiver(targetAgent);
        try {
            info("Sending Message to %s:%s", targetAgent, content);
            aclMsg.setContentObject(content);
            myAgent.send(aclMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void info(String format, Object... args) {
        logger.log(Level.INFO, String.format(format + "\n", args));
    }
}
