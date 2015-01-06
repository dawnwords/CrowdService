package service.shcomputer.cs.takepic.impl;

import android.graphics.Bitmap;
import android.location.Location;
import edu.fudan.se.crowdservice.core.ConcreteService;
import service.shcomputer.cs.takepic.interfaces.TakePictureOfURLService;

/**
 * Created by Dawnwords on 2015/1/6.
 */
public class TakePictureOfURLServiceImpl extends ConcreteService implements TakePictureOfURLService {
    @Override
    public Bitmap takeRealPictureOfURL(String url, Location location) {
        return null;
    }

    @Override
    protected Class getServiceInterface() {
        return TakePictureOfURLService.class;
    }
}
