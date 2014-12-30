package edu.fudan.se.crowdservice.core;

import jade.util.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public abstract class Template implements BundleActivator {

    private BundleContext context;

    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        this.context = bundleContext;
        log("Start Template...");
        context.registerService(Template.class, this, null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
    }

    public void executeTemplate() {
        log("Resolve Service...");
        resolveService(new ServiceResolver());
        log("Execute Template");
        execute();
    }

    protected abstract void resolveService(ServiceResolver serviceResolver);

    protected abstract void execute();

    protected void log(String msg) {
        logger.log(Level.INFO, msg);
    }

    protected class ServiceResolver {
        public <T> T resolveService(Class<T> serviceClass) {
            return context.getService(context.getServiceReference(serviceClass));
        }
    }
}
