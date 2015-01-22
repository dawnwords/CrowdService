package edu.fudan.se.crowdservice.jade.agent.behaviour;

import edu.fudan.se.crowdservice.jade.agent.ConversationType;
import edu.fudan.se.crowdservice.wrapper.ResponseWrapper;

public class SendResponseBehaviour extends MessageSendingBehaviour<ResponseWrapper> {

    public SendResponseBehaviour(ResponseWrapper response) {
        super(ConversationType.RESPONSE, response);
    }

}
