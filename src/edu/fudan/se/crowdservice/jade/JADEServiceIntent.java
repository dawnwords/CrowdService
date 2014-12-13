package edu.fudan.se.crowdservice.jade;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Dawnwords on 2014/12/13.
 */
public class JADEServiceIntent extends Intent {

    private static final String AGENT_NAME = "agent-name";

    public JADEServiceIntent(Context context) {
        super(context, JADEService.class);
    }

    public String getAgentName() {
        return getStringExtra(AGENT_NAME);
    }

    public void setAgentName(String agentName) {
        putExtra(AGENT_NAME, agentName);
    }
}
