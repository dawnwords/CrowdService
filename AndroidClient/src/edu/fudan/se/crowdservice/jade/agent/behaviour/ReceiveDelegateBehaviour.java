package edu.fudan.se.crowdservice.jade.agent.behaviour;

import android.os.Handler;
import edu.fudan.se.crowdservice.jade.agent.ConversationType;
import edu.fudan.se.crowdservice.jade.agent.uimessage.DelegateMessage;
import edu.fudan.se.crowdservice.jade.agent.uimessage.UIMessage;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;

import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class ReceiveDelegateBehaviour extends MessageReceivingBehaviour<ArrayList<KeyValueHolder>> {
    public ReceiveDelegateBehaviour(Handler handler) {
        super(ConversationType.DELEGATE, handler);
    }


    @Override
    protected UIMessage prepareMessage(ArrayList<KeyValueHolder> content) {
        return new DelegateMessage(content);
    }
}
