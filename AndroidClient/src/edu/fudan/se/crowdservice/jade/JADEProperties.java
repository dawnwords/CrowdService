package edu.fudan.se.crowdservice.jade;

import jade.android.AndroidHelper;
import jade.core.Profile;
import jade.util.leap.Properties;

/**
 * Created by Dawnwords on 2014/12/13.
 */
public class JADEProperties {
    private static final String HOST = "10.131.253.211";
    private static final String PORT = "1099";
    private static Properties instance;

    public static Properties getInstance() {
        if (instance == null) {
            instance = new Properties();
            instance.setProperty(Profile.MAIN_HOST, HOST);
            instance.setProperty(Profile.MAIN_PORT, PORT);
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
