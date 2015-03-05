package edu.fudan.se.crowdservice.jade.agent.uimessage;

import edu.fudan.se.crowdservice.wrapper.Wrapper;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class TaskMessage extends UIMessage<Wrapper> {
    public static final int WHAT = 2324;

    public TaskMessage(Wrapper value) {
        super(WHAT, value);
    }
}
