package edu.fudan.se.crowdservice.jade.agent.behaviour;

import android.os.Handler;
import edu.fudan.se.crowdservice.jade.agent.ConversationType;
import edu.fudan.se.crowdservice.jade.agent.uimessage.RequestMessage;
import edu.fudan.se.crowdservice.jade.agent.uimessage.UIMessage;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class ReceiveRequestBehaviour extends MessageReceivingBehaviour<String> {
    public ReceiveRequestBehaviour(Handler handler) {
        super(ConversationType.REQUEST, handler);
    }

    @Override
    protected UIMessage prepareMessage(String content) {
        return new RequestMessage(content);
    }
}
