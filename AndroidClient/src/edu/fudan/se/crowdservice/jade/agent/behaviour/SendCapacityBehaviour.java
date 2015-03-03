package edu.fudan.se.crowdservice.jade.agent.behaviour;

import edu.fudan.se.crowdservice.wrapper.ConversationType;

/**
 * Created by Jiahuan on 2015/1/21.
 */
public class SendCapacityBehaviour extends MessageSendingBehaviour<String> {

    public SendCapacityBehaviour(String capability) {
        super(ConversationType.CAPACITY, capability);
    }


}
