package service.shcomputer.ns.showpic.impl;

import android.net.Uri;
import android.os.Bundle;
import edu.fudan.se.crowdservice.core.ConcreteService;
import edu.fudan.se.crowdservice.core.ServiceActivity;
import service.shcomputer.ns.showpic.activity.ShowBitmapActivity;
import service.shcomputer.ns.showpic.interfaces.ShowPictureService;

/**
 * Created by Dawnwords on 2015/1/6.
 */
public class ShowPictureServiceImpl extends ConcreteService implements ShowPictureService {

    @Override
    protected Class getServiceInterface() {
        return ShowPictureService.class;
    }

    @Override
    protected Class<? extends ServiceActivity> getServiceActivity() {
        return ShowBitmapActivity.class;
    }

    @Override
    public void showPic(String pic) {
        Bundle bundle = new Bundle();
        bundle.putString(ShowBitmapActivity.BITMAP, pic);
        startServiceActivity(bundle);
    }
}
