package edu.fudan.se.crowdservice.jade.agent.behaviour;

import edu.fudan.se.crowdservice.jade.agent.ConversationType;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.wrapper.ResponseWrapper;

import java.util.ArrayList;

public class SendResponseBehaviour extends MessageSendingBehaviour<ResponseWrapper> {

    public SendResponseBehaviour(long taskId, ArrayList<KeyValueHolder> res) {
        super(ConversationType.RESPONSE, new ResponseWrapper(taskId,res));
    }

}
