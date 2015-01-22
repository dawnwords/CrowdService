package edu.fudan.se.crowdservice.jade.agent.behaviour;

import edu.fudan.se.crowdservice.bean.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.jade.agent.ConversationType;

import java.util.ArrayList;

public class SendResponseBehaviour extends MessageSendingBehaviour<ArrayList<KeyValueHolder>> {

    public SendResponseBehaviour(ArrayList<KeyValueHolder> keyValueHolders) {
        super(ConversationType.RESPONSE, keyValueHolders);
    }

}
