package edu.fudan.se.crowdservice.jade.agent.uimessage;


import edu.fudan.se.crowdservice.kv.ImageDisplay;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.wrapper.DelegateWrapper;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class DelegateMessage extends UIMessage<DelegateWrapper> {
    public static final int DELEGATE = 1442;

    public DelegateMessage(DelegateWrapper value) {
        super(DELEGATE, value);
    }
}
