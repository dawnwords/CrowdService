package edu.fudan.se.crowdservice.core;

/**
 * Created by Dawnwords on 2014/4/22.
 */
public class ActivityResult {
    private static ActivityResult ourInstance = new ActivityResult();
    private ResultHolder serviceActivityResultHolder;

    private ActivityResult() {
    }

    public static ActivityResult getInstance() {
        return ourInstance;
    }

    public ResultHolder getServiceActivityResultHolder() {
        return serviceActivityResultHolder;
    }

    public void setServiceActivityResultHolder(ResultHolder serviceActivityResultHolder) {
        this.serviceActivityResultHolder = serviceActivityResultHolder;
    }
}
