package edu.fudan.se.crowdservice.jade.agent;

import android.location.Location;
import android.os.Handler;
import edu.fudan.se.crowdservice.core.Template;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;

import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/21.
 */
public interface AgentInterface {
    void registerHandler(Handler handler);

    void sendCapacity(String capacity);

    void setLocation(Location myLocation);

    void sendResponse(long taskId, ArrayList<KeyValueHolder> response);

    void executeTemplate(Template template);
}
