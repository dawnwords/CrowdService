package edu.fudan.se.crowdservice.jade.agent.behaviour;

import android.os.Handler;
import android.os.Message;
import edu.fudan.se.crowdservice.jade.agent.ConversationType;
import edu.fudan.se.crowdservice.jade.agent.uimessage.UIMessage;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.Serializable;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public abstract class MessageReceivingBehaviour<T extends Serializable> extends CyclicBehaviour {

    private ConversationType conversationType;
    private Handler handler;

    public MessageReceivingBehaviour(ConversationType conversationType, Handler handler) {
        this.conversationType = conversationType;
        this.handler = handler;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                MessageTemplate.MatchConversationId(conversationType.name()));
        ACLMessage aclMsg = myAgent.receive(mt);
        if (aclMsg != null) {
            try {
                UIMessage message = prepareMessage((T) aclMsg.getContentObject()) ;
                handler.sendMessage(message.asMessage());
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        } else {
            block();
        }
    }

    protected abstract UIMessage prepareMessage(T content);

}