package edu.fudan.se.crowdservice.jade.agent.behaviour;

import android.os.Handler;
import edu.fudan.se.crowdservice.wrapper.ConversationType;
import edu.fudan.se.crowdservice.jade.agent.uimessage.RequestMessage;
import edu.fudan.se.crowdservice.jade.agent.uimessage.UIMessage;
import edu.fudan.se.crowdservice.wrapper.RequestWrapper;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class ReceiveRequestBehaviour extends MessageReceivingBehaviour<RequestWrapper> {
    public ReceiveRequestBehaviour(Handler handler) {
        super(ConversationType.REQUEST, handler);
    }

    @Override
    protected UIMessage prepareMessage(RequestWrapper content) {
        return new RequestMessage(content);
    }
}
