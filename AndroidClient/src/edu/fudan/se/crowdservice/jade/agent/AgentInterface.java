package edu.fudan.se.crowdservice.jade.agent;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import edu.fudan.se.crowdservice.core.Template;
import edu.fudan.se.crowdservice.core.TemplateFactory;
import edu.fudan.se.crowdservice.wrapper.OfferWrapper;
import edu.fudan.se.crowdservice.wrapper.ResponseWrapper;

/**
 * Created by Jiahuan on 2015/1/21.
 */
public interface AgentInterface {
    void registerHandler(Handler handler);

    void sendCapacity(String capacity);

    void sendOffer(OfferWrapper offer);

    void sendResponse(ResponseWrapper response);

    void setLocation(Location myLocation);

    void setContext(Context context);

    void setResultInput(int sessionId, String resultInput);

    int executeTemplate(TemplateFactory templateFactory);
}
