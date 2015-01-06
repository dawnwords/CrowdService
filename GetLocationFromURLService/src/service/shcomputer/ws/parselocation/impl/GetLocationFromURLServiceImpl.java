package service.shcomputer.ws.parselocation.impl;

import android.location.Location;
import edu.fudan.se.crowdservice.core.ConcreteService;
import service.shcomputer.ws.parselocation.interfaces.GetLocationFromURLService;

/**
 * Created by Dawnwords on 2015/1/6.
 */
public class GetLocationFromURLServiceImpl extends ConcreteService implements GetLocationFromURLService {
    @Override
    protected Class getServiceInterface() {
        return GetLocationFromURLService.class;
    }

    @Override
    public Location getLocationFromURL(String url) {
        return null;
    }
}
