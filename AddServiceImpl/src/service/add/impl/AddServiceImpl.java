package service.add.impl;

import edu.fudan.se.crowdservice.core.ConcreteService;
import service.add.interfaces.AddService;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public class AddServiceImpl extends ConcreteService implements AddService {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    protected boolean isCrowd() {
        return false;
    }
}
