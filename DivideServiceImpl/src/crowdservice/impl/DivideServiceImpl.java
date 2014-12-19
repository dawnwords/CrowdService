package crowdservice.impl;

import crowdservice.interfaces.DivideService;
import edu.fudan.se.crowdservice.core.ConcreteService;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public class DivideServiceImpl extends ConcreteService implements DivideService {

    @Override
    public double div(double a, double b) {
        return a / b;
    }

    @Override
    protected Class getServiceInterface() {
        return DivideService.class;
    }
}
