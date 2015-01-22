package edu.fudan.se.crowdservice.jade;

import android.location.Location;
import android.os.Binder;
import android.os.Handler;
import edu.fudan.se.crowdservice.bean.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.core.Template;
import edu.fudan.se.crowdservice.jade.agent.AgentInterface;

import java.util.ArrayList;

/**
 * Created by Dawnwords on 2014/12/13.
 */
public class AgentManager extends Binder implements AgentInterface {
    private AgentInterface agent;

    public void setAgent(AgentInterface agent) {
        this.agent = agent;
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
    public void setLocation(Location myLocation) {
        agent.setLocation(myLocation);
    }

    @Override
    public void sendResponse(ArrayList<KeyValueHolder> response) {
        agent.sendResponse(response);
    }

    @Override
    public void executeTemplate(Template template) {
        agent.executeTemplate(template);
    }
}
