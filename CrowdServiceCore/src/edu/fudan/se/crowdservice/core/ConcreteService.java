package edu.fudan.se.crowdservice.core;

import jade.util.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/18.
 */
public abstract class ConcreteService implements BundleActivator {
    private Logger logger = Logger.getJADELogger(ConcreteService.class.getName());

    @Override
    public final void start(BundleContext context) throws Exception {
        String serviceName = getServiceInterface().getName();
        logger.log(Level.INFO, "Register Service:" + serviceName);
        context.registerService(serviceName, this, null);
    }

    @Override
    public final void stop(BundleContext context) throws Exception {
    }

    protected abstract Class getServiceInterface();
}
