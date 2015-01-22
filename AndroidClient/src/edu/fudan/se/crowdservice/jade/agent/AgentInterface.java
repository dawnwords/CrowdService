package edu.fudan.se.crowdservice.jade.agent;

import android.location.Location;
import edu.fudan.se.crowdservice.bean.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.core.Template;

import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/21.
 */
public interface AgentInterface {
    void sendCapacity(String capacity);
    void sendCustomLocation(Location myLocation);
    void sendResponse(ArrayList<KeyValueHolder> response);
    void executeTemplate(Template template);
}
