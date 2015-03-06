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
        addService = serviceResolver.resolveService(AddService.class, 0.5, 0.5);
        divideService = serviceResolver.resolveService(DivideService.class, 0.5, 0.5);
    }

    @Override
    public void execute() {
        double result;
        int add1 = Integer.parseInt(requestUserInput("Input Add 1"));
        int add2 = Integer.parseInt(requestUserInput("Input Add 2"));
        result = addService.add(add1, add2);
        int divisor = Integer.parseInt(requestUserInput("Input Divisor"));
        result = divideService.div(result, divisor);
        showMessage("result is " + result);
    }

}
