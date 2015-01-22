package edu.fudan.se.crowdservice.jade.agent.behaviour;

import jade.core.AID;
import jade.core.Agent;
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
    private AID targetId;
    private Agent agent;
    private ConversationType conversationId;
    private T content;

    public MessageSendingBehaviour(Agent agent, String targetAgentName, ConversationType conversationId,T content) {
        this.targetId = new AID(targetAgentName, true);
        this.agent = agent;
        this.conversationId = conversationId;
        this.content = content;
    }

    @Override
    public void action() {
        ACLMessage aclMsg = new ACLMessage(ACLMessage.INFORM);
        aclMsg.setConversationId(conversationId.name());
        aclMsg.addReceiver(targetId);
        try {
            info("Sending Message to %s:%s", targetId, content);
            aclMsg.setContentObject(content);
            agent.send(aclMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void info(String format, Object... args) {
        logger.log(Level.INFO, String.format(format + "\n", args));
    }

    public static enum ConversationType {
        REGISTER_CAPACITY, RESPONSE, OFFER
    }

}
