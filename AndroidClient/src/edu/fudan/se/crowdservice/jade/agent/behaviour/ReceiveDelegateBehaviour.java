package edu.fudan.se.crowdservice.jade.agent.behaviour;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import edu.fudan.se.crowdservice.core.ResultHolder;
import edu.fudan.se.crowdservice.jade.agent.ConversationType;

import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class ReceiveDelegateBehaviour extends MessageReceivingBehaviour<ArrayList<ResultHolder>> {
    public ReceiveDelegateBehaviour(Handler handler) {
        super(ConversationType.DELEGATE, handler);
    }

    @Override
    protected void handleMessage(Handler handler, ArrayList<ResultHolder> content) {
        //TODO finish delegate message handling logic
    }
}
