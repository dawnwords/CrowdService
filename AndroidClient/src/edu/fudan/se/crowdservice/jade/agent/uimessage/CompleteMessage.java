package edu.fudan.se.crowdservice.jade.agent.uimessage;

import edu.fudan.se.crowdservice.wrapper.CompleteWrapper;
import edu.fudan.se.crowdservice.wrapper.RefuseWrapper;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class CompleteMessage extends UIMessage<CompleteWrapper> {
    public static final int WHAT = 2324;

    public CompleteMessage(CompleteWrapper value) {
        super(WHAT, value);
    }
}
