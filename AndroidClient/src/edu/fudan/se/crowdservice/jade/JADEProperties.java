package edu.fudan.se.crowdservice.jade;

import jade.android.AndroidHelper;
import jade.core.Profile;
import jade.util.leap.Properties;

/**
 * Created by Dawnwords on 2014/12/13.
 */
public class JADEProperties {
    private static Properties instance;

    public static Properties getInstance(String jadeIP, int jadePort) {
        if (instance == null) {
            instance = new Properties();
            instance.setProperty(Profile.MAIN_HOST, jadeIP);
            instance.setProperty(Profile.MAIN_PORT, String.valueOf(jadePort));
            instance.setProperty(Profile.MAIN, Boolean.FALSE.toString());
            instance.setProperty(Profile.JVM, Profile.ANDROID);

            if (AndroidHelper.isEmulator()) {
                // for Emulator
                instance.setProperty(Profile.LOCAL_HOST, AndroidHelper.LOOPBACK);
                instance.setProperty(Profile.LOCAL_PORT, "2000");
            } else {
                instance.setProperty(Profile.LOCAL_HOST, AndroidHelper.getLocalIPAddress());
            }
        }
        return instance;
    }

}
