package edu.fudan.se.crowdservice.jade.agent.behaviour;

import android.os.Handler;
import edu.fudan.se.crowdservice.jade.agent.ConversationType;
import edu.fudan.se.crowdservice.jade.agent.uimessage.RefuseMessage;
import edu.fudan.se.crowdservice.jade.agent.uimessage.UIMessage;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class ReceiveRefuseBehaviour extends MessageReceivingBehaviour<String> {
    public ReceiveRefuseBehaviour(Handler handler) {
        super(ConversationType.REFUSE, handler);
    }


    @Override
    protected UIMessage prepareMessage(String content) {
        return new RefuseMessage(content);
    }
}
