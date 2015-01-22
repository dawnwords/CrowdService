package edu.fudan.se.crowdservice.jade.agent.behaviour;

import android.os.Handler;
import edu.fudan.se.crowdservice.jade.agent.ConversationType;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class ReceiveRefuseBehaviour extends MessageReceivingBehaviour<String> {
    public ReceiveRefuseBehaviour(Handler handler) {
        super(ConversationType.REFUSE, handler);
    }

    @Override
    protected void handleMessage(Handler handler, String content) {
        //TODO finish handling refuse logic
    }
}
