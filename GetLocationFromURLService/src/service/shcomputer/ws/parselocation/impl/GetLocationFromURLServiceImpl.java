package service.shcomputer.ws.parselocation.impl;

import android.location.Location;
import edu.fudan.se.crowdservice.core.ConcreteService;
import service.shcomputer.ws.parselocation.interfaces.GetLocationFromURLService;

/**
 * Created by Dawnwords on 2015/1/6.
 */
public class GetLocationFromURLServiceImpl extends ConcreteService implements GetLocationFromURLService {

    @Override
    public Location getLocationFromURL(String url) {
        Location l = new Location("");
        l.setLongitude(121.6004);
        l.setLatitude(31.1977);
        return l;
    }

    @Override
    protected boolean isCrowd() {
        return false;
    }
}
