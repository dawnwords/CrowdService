package edu.fudan.se.crowdservice.jade.agent.uimessage;

import edu.fudan.se.crowdservice.data.ConsumerSession;

/**
 * Created by Dawnwords on 2015/2/25.
 */
public class ConsumerSessionMessage extends UIMessage<ConsumerSession.Message> {
    public static final int WHAT = 945;

    public ConsumerSessionMessage(ConsumerSession.Message value) {
        super(WHAT, value);
    }
}
