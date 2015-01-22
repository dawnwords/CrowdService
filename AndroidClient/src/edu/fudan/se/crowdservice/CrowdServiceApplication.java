package edu.fudan.se.crowdservice;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import edu.fudan.se.crowdservice.felix.FelixService;
import edu.fudan.se.crowdservice.jade.JADEService;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class CrowdServiceApplication extends Application {

    public static final String AGENT_NAME = "agent_name";
    public static final String CAPACITY = "capacity";
    public static final String CROWD_SERVICE = "crowd_service";
    private SharedPreferences settings;

    @Override
    public void onCreate() {
        super.onCreate();

        settings = getSharedPreferences(CROWD_SERVICE, 0);
        if (checkEmpty(AGENT_NAME) && checkEmpty(CAPACITY)) {
            startService(new Intent(getApplicationContext(), JADEService.class));
            startService(new Intent(getApplicationContext(), FelixService.class));
        }
    }

    public boolean checkEmpty(String key) {
        return settings.getString(key, "").isEmpty();
    }
}
