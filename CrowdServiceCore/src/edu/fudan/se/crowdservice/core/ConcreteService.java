package edu.fudan.se.crowdservice.core;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Created by Dawnwords on 2014/12/18.
 */
public abstract class ConcreteService implements BundleActivator {
    @Override
    public final void start(BundleContext context) throws Exception {
        context.registerService(getServiceInterface().getName(), this, null);
    }

    @Override
    public final void stop(BundleContext context) throws Exception {
    }

    protected abstract Class getServiceInterface();
}
