package service.divide.impl;

import edu.fudan.se.crowdservice.core.ConcreteService;
import service.divide.interfaces.DivideService;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public class DivideServiceImpl extends ConcreteService implements DivideService {

    @Override
    public double div(double a, double b) {
        return a / b;
    }

    @Override
    protected boolean isCrowd() {
        return false;
    }
}
