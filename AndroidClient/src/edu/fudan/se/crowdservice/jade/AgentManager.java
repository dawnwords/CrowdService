package edu.fudan.se.crowdservice.jade;

import android.location.Location;
import android.os.Binder;
import android.os.Handler;
import edu.fudan.se.crowdservice.core.Template;
import edu.fudan.se.crowdservice.jade.agent.AgentInterface;
import edu.fudan.se.crowdservice.wrapper.OfferWrapper;
import edu.fudan.se.crowdservice.wrapper.ResponseWrapper;

/**
 * Created by Dawnwords on 2014/12/13.
 */
public class AgentManager extends Binder implements AgentInterface {
    private AgentInterface agent;
    private OnAgentReadyInterface isAgentReady;

    public void setAgent(AgentInterface agent) {
        if (agent == null) {
            isAgentReady.onAgentFail();
        } else {
            this.agent = agent;
            isAgentReady.onAgentReady();
        }
    }

    public void registerOnAgentReadyInterface(OnAgentReadyInterface isAgentReady) {
        this.isAgentReady = isAgentReady;
    }

    @Override
    public void registerHandler(Handler handler) {
        agent.registerHandler(handler);
    }

    @Override
    public void sendCapacity(String capacity) {
        agent.sendCapacity(capacity);
    }

    @Override
    public void sendOffer(OfferWrapper offer) {
        agent.sendOffer(offer);
    }

    @Override
    public void setLocation(Location myLocation) {
        agent.setLocation(myLocation);
    }

    @Override
    public void sendResponse(ResponseWrapper response) {
        agent.sendResponse(response);
    }

    @Override
    public void executeTemplate(Template template) {
        agent.executeTemplate(template);
    }

    public static interface OnAgentReadyInterface {
        void onAgentReady();

        void onAgentFail();
    }
}
