package edu.fudan.se.crowdservice.jade.agent.behaviour;

import edu.fudan.se.crowdservice.wrapper.ConversationType;
import edu.fudan.se.crowdservice.wrapper.OfferWrapper;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class SendOfferBehaviour extends MessageSendingBehaviour<OfferWrapper> {
    public SendOfferBehaviour(OfferWrapper offer) {
        super(ConversationType.OFFER, offer);
    }
}
