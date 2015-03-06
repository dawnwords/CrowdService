package edu.fudan.se.crowdservice.core;

import android.content.Context;
import jade.util.Logger;
import org.osgi.framework.*;

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
    private AtomicInteger instanceCount;

    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;
        this.serviceReferences = new Stack<ServiceReference>();
        this.instanceCount = new AtomicInteger();
        log("Start Template Factory...");
        Hashtable properties = new Hashtable();
        properties.put(Constants.BUNDLE_SYMBOLICNAME, getTemplateClass().getSimpleName());
        serviceReferences.push(this.bundleContext.registerService(TemplateFactory.class, this, properties).getReference());
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public synchronized T createTemplateInstance(ServiceExecutionListener listener) {
        T template = null;
        try {
            template = getTemplateClass().newInstance();
            template.setServiceExecutionListener(new TemplateStopListener(listener));
            template.setServiceResolver(new ServiceResolver());
            instanceCount.incrementAndGet();
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
        public <S> S resolveService(Class<S> serviceClass) {
            ServiceReference<S> serviceReference = bundleContext.getServiceReference(serviceClass);
            serviceReferences.push(serviceReference);
            S service = bundleContext.getService(serviceReference);
            initConcreteService((ConcreteService) service);
            return service;
        }

        private void initConcreteService(ConcreteService service) {
            service.setContext(context);
            service.setTemplateName(getTemplateClass().getSimpleName());
        }
    }

    private class TemplateStopListener implements ServiceExecutionListener {
        private ServiceExecutionListener listener;

        public TemplateStopListener(ServiceExecutionListener listener) {
            this.listener = listener;
        }

        @Override
        public void onServiceStart(Class serviceClass) {
            listener.onServiceStart(serviceClass);
        }

        @Override
        public void onServiceStop(Class serviceClass) {
            listener.onServiceStop(serviceClass);
        }

        @Override
        public void onServiceException(Class serviceClass, String reason) {
            listener.onServiceException(serviceClass, reason);
        }

        @Override
        public void onTemplateStop() {
            listener.onTemplateStop();
            if (instanceCount.decrementAndGet() == 0) {
                uninstallTemplateFactory();
            }
        }

        @Override
        public String onRequestUserInput(String msg) {
            return listener.onRequestUserInput(msg);
        }

        @Override
        public boolean onRequestUserConfirm(String msg) {
            return listener.onRequestUserConfirm(msg);
        }

        @Override
        public int onRequestUserChoose(String msg, String[] items) {
            return listener.onRequestUserChoose(msg, items);
        }

        @Override
        public void onShowMessage(String msg) {
            listener.onShowMessage(msg);
        }
    }
}
