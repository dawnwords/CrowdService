package edu.fudan.se.crowdservice.template;

import crowdservice.interfaces.AddService;
import crowdservice.interfaces.DivideService;
import edu.fudan.se.crowdservice.core.AbstractService;
import edu.fudan.se.crowdservice.core.Template;
import jade.util.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public class ArithmeticTemplate extends Template implements BundleActivator {
    @AbstractService
    private AddService addService;
    @AbstractService
    private DivideService divideService;

    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    @Override
    public void execute() {
        double result;
        result = addService.add(3, 5);
        result = divideService.div(result, 2);
        logger.log(Level.INFO, "result is " + result);
    }

    @Override
    public void start(BundleContext context) throws Exception {
        logger.log(Level.INFO, "Start Template...");
        context.registerService(Template.class, this, null);
        logger.log(Level.INFO, "Template Service Registered!");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    }
}
