package service.shcomputer.ws.select.impl;

import edu.fudan.se.crowdservice.core.ConcreteService;
import service.shcomputer.ws.select.interfaces.SelectURLService;

/**
 * Created by Dawnwords on 2015/1/6.
 */
public class SelectURLServiceImpl extends ConcreteService implements SelectURLService {

    @Override
    protected Class getServiceInterface() {
        return SelectURLService.class;
    }

    @Override
    public String getSelectedURL(String sourceURL) {
        return null;
    }
}
