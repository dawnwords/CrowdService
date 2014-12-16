package service.template;

import edu.fudan.se.crowdservice.core.Service;
import edu.fudan.se.crowdservice.core.Template;
import jade.util.Logger;
import service.interfaces.AddService;
import service.interfaces.DivideService;

import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public class ArithmeticTemplate extends Template {
    @Service
    private AddService addService;
    @Service
    private DivideService divideService;

    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    @Override
    public void execute() {
        double result;
        result = addService.add(3, 5);
        result = divideService.div(result, 2);
        logger.log(Level.INFO, "result is " + result);
    }
}
