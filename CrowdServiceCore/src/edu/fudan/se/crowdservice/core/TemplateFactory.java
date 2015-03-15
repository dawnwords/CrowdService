package edu.fudan.se.crowdservice.core;

import android.content.Context;
import android.content.SharedPreferences;
import jade.util.Logger;
import org.osgi.framework.*;

import java.util.Date;
import java.util.Hashtable;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 * Created by Dawnwords on 2015/3/6.
 */
public abstract class TemplateFactory<T extends Template> implements BundleActivator {
    private BundleContext bundleContext;
    private Context context;
    private Stack<ServiceReference> serviceReferences;
    private InstanceCount instanceCount;
    private SharedPreferences settings;

    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;
        this.serviceReferences = new Stack<ServiceReference>();
        this.instanceCount = new InstanceCount();
        log("Start Template Factory...");
        Hashtable properties = new Hashtable();
        properties.put(Constants.BUNDLE_SYMBOLICNAME, getTemplateClass().getSimpleName());
        this.serviceReferences.push(bundleContext.registerService(TemplateFactory.class, this, properties).getReference());
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
    }

    public void setContext(Context context) {
        this.context = context;
        this.settings = context.getSharedPreferences(SavedProperty.CROWD_SERVICE, 0);
    }

    public String templateName() {
        return getTemplateClass().getSimpleName();
    }

    public synchronized T createTemplateInstance(ServiceExecutionListener listener) {
        T template = null;
        try {
            template = getTemplateClass().newInstance();
            template.setInstanceCount(instanceCount.createInstance());
            template.setServiceResolver(new ServiceResolver());
            template.setConsumerId(settings.getString(SavedProperty.AGENT_NAME, ""));
            template.setServiceExecutionListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return template;
    }

    private synchronized void uninstallTemplateFactory() {
        while (!serviceReferences.empty()) {
            Bundle bundle = serviceReferences.pop().getBundle();
            log("Uninstall Bundle:" + bundle.getSymbolicName());
            try {
                bundle.uninstall();
            } catch (BundleException e) {
                e.printStackTrace();
            }
        }
    }

    protected void log(String msg) {
        logger.log(Level.INFO, msg);
    }

    protected abstract Class<T> getTemplateClass();

    class ServiceResolver {
        private long time = new Date().getTime();

        public <S> S resolveService(Class<S> serviceClass) {
            ServiceReference<S> serviceReference = bundleContext.getServiceReference(serviceClass);
            if (!serviceReferences.contains(serviceReference)) {
                serviceReferences.push(serviceReference);
            }
            S service = bundleContext.getService(serviceReference);
            initConcreteService((ConcreteService) service);
            return service;
        }

        private void initConcreteService(ConcreteService service) {
            service.setContext(context);
            service.templateName = getTemplateClass().getSimpleName() + "-" + time;
        }
    }

    class InstanceCount {
        private AtomicInteger count = new AtomicInteger();

        public InstanceCount createInstance() {
            count.incrementAndGet();
            return this;
        }

        public void destroyInstance() {
            if (count.decrementAndGet() == 0) {
                uninstallTemplateFactory();
            }
        }
    }
}
