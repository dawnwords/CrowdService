package edu.fudan.se.crowdservice.jade.agent;

import jade.core.AID;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public enum ConversationType {
    CAPACITY(ConversationType.CHECKIN_AGENT),
    HEARTBEAT(ConversationType.CHECKIN_AGENT),
    RESPONSE, OFFER, DELEGATE, REFUSE, REQUEST;

    private static final String TASK_AGENT = "task-agent";
    private static final String CHECKIN_AGENT = "checkin-agent";

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
