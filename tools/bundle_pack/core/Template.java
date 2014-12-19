package edu.fudan.se.crowdservice.core;

import jade.util.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public abstract class Template implements BundleActivator {
    private Logger logger = Logger.getJADELogger(Template.class.getName());

    @Override
    public final void start(BundleContext context) throws Exception {
        logger.log(Level.INFO, "Template Start...");
        context.registerService(Template.class.getName(), this, null);
        logger.log(Level.INFO, "Template Service Registered!");
    }

    @Override
    public final void stop(BundleContext context) throws Exception {
    }

    public abstract void execute();
}
