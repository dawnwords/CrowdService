package edu.fudan.se.crowdservice.jade.agent.behaviour;

import android.os.Handler;
import edu.fudan.se.crowdservice.jade.agent.uimessage.CompleteMessage;
import edu.fudan.se.crowdservice.jade.agent.uimessage.UIMessage;
import edu.fudan.se.crowdservice.wrapper.CompleteWrapper;
import edu.fudan.se.crowdservice.wrapper.ConversationType;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class ReceiveCompleteBehaviour extends MessageReceivingBehaviour<CompleteWrapper> {
    public ReceiveCompleteBehaviour(Handler handler) {
        super(ConversationType.REFUSE, handler);
    }

    @Override
    protected UIMessage prepareMessage(CompleteWrapper content) {
        return new CompleteMessage(content);
    }
}
