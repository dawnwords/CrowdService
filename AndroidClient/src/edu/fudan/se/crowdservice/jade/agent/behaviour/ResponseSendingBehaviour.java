package edu.fudan.se.crowdservice.jade.agent.behaviour;

import edu.fudan.se.crowdservice.bean.kv.KeyValueHolder;
import jade.core.Agent;

import java.util.ArrayList;

public class ResponseSendingBehaviour extends MessageSendingBehaviour<ArrayList<KeyValueHolder>> {

    public ResponseSendingBehaviour(Agent a, ArrayList<KeyValueHolder> keyValueHolders) {
        super(a, "task-agent", ConversationType.RESPONSE, keyValueHolders);
    }

}
