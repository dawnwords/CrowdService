package edu.fudan.se.crowdservice.template;

import edu.fudan.se.crowdservice.core.Template;
import service.add.interfaces.AddService;
import service.divide.interfaces.DivideService;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public class ArithmeticTemplate extends Template {
    private AddService addService;
    private DivideService divideService;

    @Override
    protected void resolveService(ServiceResolver serviceResolver) {
        addService = serviceResolver.resolveService(AddService.class);
        divideService = serviceResolver.resolveService(DivideService.class);
    }

    @Override
    public void execute() {
        double result;
        result = addService.add(3, 5);
        result = divideService.div(result, 2);
        log("result is " + result);
    }

}
