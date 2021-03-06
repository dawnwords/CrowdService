package edu.fudan.se.crowdservice.wrapper;

import jade.core.AID;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public enum ConversationType {
    CAPACITY(ConversationType.CHECKIN_AGENT),
    HEARTBEAT(ConversationType.CHECKIN_AGENT),
    RESPONSE, OFFER, DELEGATE, REFUSE, REQUEST, COMPLETE;

    public static final String TASK_AGENT = "task-agent";
    public static final String CHECKIN_AGENT = "checkin-agent";

    private String targetAgent;

    ConversationType() {
        this(ConversationType.TASK_AGENT);
    }

    ConversationType(String targetAgent) {
        this.targetAgent = targetAgent;
    }

    public AID target() {
        return new AID(targetAgent, false);
    }
}
