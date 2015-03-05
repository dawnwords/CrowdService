package edu.fudan.se.crowdservice.jade.agent.behaviour;

import android.os.Handler;
import edu.fudan.se.crowdservice.jade.agent.uimessage.TaskMessage;
import edu.fudan.se.crowdservice.jade.agent.uimessage.UIMessage;
import edu.fudan.se.crowdservice.wrapper.ConversationType;
import edu.fudan.se.crowdservice.wrapper.RefuseWrapper;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class ReceiveRefuseBehaviour extends MessageReceivingBehaviour<RefuseWrapper> {
    public ReceiveRefuseBehaviour(Handler handler) {
        super(ConversationType.REFUSE, handler);
    }

    @Override
    protected UIMessage prepareMessage(RefuseWrapper content) {
        return new TaskMessage(content);
    }
}
