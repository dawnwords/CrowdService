package service.shcomputer.ws.select.impl;

import android.os.Bundle;
import edu.fudan.se.crowdservice.core.ConcreteService;
import edu.fudan.se.crowdservice.core.ServiceActivity;
import service.shcomputer.ws.select.activity.WebViewerActivity;
import service.shcomputer.ws.select.interfaces.SelectURLService;

/**
 * Created by Dawnwords on 2015/1/6.
 */
public class SelectURLServiceImpl extends ConcreteService implements SelectURLService {

    public static final String START_URL = "START_URL";

    @Override
    public String getSelectedURL(String sourceURL) {
        Bundle bundle = new Bundle();
        bundle.putString(START_URL, sourceURL);
        return startServiceActivity(bundle);
    }

    @Override
    protected Class<? extends ServiceActivity> getServiceActivity() {
        return WebViewerActivity.class;
    }

    @Override
    protected boolean isCrowd() {
        return false;
    }
}
