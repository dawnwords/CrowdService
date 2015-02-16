package edu.fudan.se.crowdservice.jade.agent.uimessage;


import edu.fudan.se.crowdservice.wrapper.RequestWrapper;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class RequestMessage extends UIMessage<RequestWrapper> {
    public static final int REQUEST = 1435;

    public RequestMessage(RequestWrapper value) {
        super(REQUEST, value);
    }
}
