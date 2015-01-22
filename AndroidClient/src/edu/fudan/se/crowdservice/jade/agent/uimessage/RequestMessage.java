package edu.fudan.se.crowdservice.jade.agent.uimessage;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class RequestMessage extends UIMessage<String> {
    public static int REQUEST_MESSAGE = 1435;

    public RequestMessage(String value) {
        super(REQUEST_MESSAGE, value);
    }
}
