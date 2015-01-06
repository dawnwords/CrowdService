package service.shcomputer.ns.showpic.impl;

import android.graphics.Bitmap;
import edu.fudan.se.crowdservice.core.ConcreteService;
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
    public void showPic(Bitmap pic) {

    }
}
