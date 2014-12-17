package edu.fudan.se.crowdservice.core;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public abstract class Template implements BundleActivator {
    @Override
    public final void start(BundleContext context) throws Exception {
        context.registerService(Template.class.getName(), this, null);
    }

    @Override
    public final void stop(BundleContext context) throws Exception {
    }

    public abstract void execute();
}
