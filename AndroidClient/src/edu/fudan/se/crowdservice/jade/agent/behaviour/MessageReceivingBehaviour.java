package edu.fudan.se.crowdservice.jade.agent.behaviour;

import android.os.Handler;
import edu.fudan.se.crowdservice.jade.agent.uimessage.TaskMessage;
import edu.fudan.se.crowdservice.wrapper.ConversationType;
import edu.fudan.se.crowdservice.wrapper.Wrapper;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public abstract class MessageReceivingBehaviour<T extends Wrapper> extends CyclicBehaviour {

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
                handler.sendMessage(new TaskMessage(prepareMessage((T) aclMsg.getContentObject())).asMessage());
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        } else {
            block();
        }
    }

    protected T prepareMessage(T content) {
        return content;
    }

}