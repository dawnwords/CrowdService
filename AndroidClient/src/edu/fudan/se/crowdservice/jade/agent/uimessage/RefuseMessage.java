package edu.fudan.se.crowdservice.jade.agent.uimessage;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class RefuseMessage extends UIMessage<String> {
    public static final int REFUSE = 1439;

    public RefuseMessage( String value) {
        super(REFUSE, value);
    }
}
