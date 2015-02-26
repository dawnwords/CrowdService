package edu.fudan.se.crowdservice.jade.agent.uimessage;

import edu.fudan.se.crowdservice.wrapper.RefuseWrapper;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class RefuseMessage extends UIMessage<RefuseWrapper> {
    public static final int WHAT = 1439;

    public RefuseMessage(RefuseWrapper value) {
        super(WHAT, value);
    }
}
