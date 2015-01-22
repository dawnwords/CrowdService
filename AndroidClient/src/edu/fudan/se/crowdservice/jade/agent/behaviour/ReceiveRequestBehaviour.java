package edu.fudan.se.crowdservice.jade.agent.behaviour;

import android.os.Handler;
import edu.fudan.se.crowdservice.jade.agent.ConversationType;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class ReceiveRequestBehaviour extends MessageReceivingBehaviour<String> {
    public ReceiveRequestBehaviour(Handler handler) {
        super(ConversationType.REQUEST, handler);
    }

    @Override
    protected void handleMessage(Handler handler, String request) {
        //TODO finish handle request logic
    }
}
