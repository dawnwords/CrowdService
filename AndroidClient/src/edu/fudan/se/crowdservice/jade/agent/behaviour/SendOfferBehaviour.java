package edu.fudan.se.crowdservice.jade.agent.behaviour;

import edu.fudan.se.crowdservice.jade.agent.ConversationType;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class SendOfferBehaviour extends MessageSendingBehaviour<Integer> {
    public SendOfferBehaviour(int price) {
        super(ConversationType.OFFER, price);
    }
}
