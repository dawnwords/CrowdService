package edu.fudan.se.crowdservice.core;

import android.content.Context;
import jade.util.Logger;
import org.osgi.framework.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Stack;
import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public abstract class Template implements BundleActivator {

    private BundleContext bundleContext;
    private Context context;
    private ServiceExecutionListener listener;
    private HashMap<String, TimeCost> serviceTimeCostMap;
    private Stack<ServiceReference> serviceReferences;

    private Logger logger = Logger.getJADELogger(this.getClass().getName());
    private TimeCost totalTimeCost;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;
        this.serviceReferences = new Stack<ServiceReference>();
        log("Start Template...");
        Hashtable properties = new Hashtable();
        properties.put(Constants.BUNDLE_SYMBOLICNAME, getTemplateName());
        serviceReferences.push(this.bundleContext.registerService(Template.class, this, properties).getReference());
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void executeTemplate(ServiceExecutionListener listener) {
        this.listener = listener;
        this.totalTimeCost = requestTotalTimeCost();
        log("Total Time=" + totalTimeCost.time + ",Cost=" + totalTimeCost.cost);
        planTotalTimeCost(getTemplateName(), totalTimeCost);
        log("Resolve Service...");
        resolveService(new ServiceResolver());
        log("Execute Template");
        execute();
        log("Stop Template");
        listener.onTemplateStop();
        uninstallTemplate();
    }

    private void uninstallTemplate() {
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

    private TimeCost requestTotalTimeCost() {
        String timeString = requestUserInput("Please input expected execution time for this template:(s)");
        String costString = requestUserInput("Please input expected cost for this template:($)");
        return new TimeCost(Integer.parseInt(timeString), Integer.parseInt(costString));
    }

    private void planTotalTimeCost(String templateName, TimeCost total) {
        //TODO invoke tfws1
        serviceTimeCostMap = new HashMap<String, TimeCost>();
    }

    protected abstract String getTemplateName();

    protected abstract void resolveService(ServiceResolver serviceResolver);

    protected abstract void execute();

    protected void log(String msg) {
        logger.log(Level.INFO, msg);
    }

    protected String requestUserInput(final String message) {
        return listener.onRequestUserInput(message);
    }

    protected boolean requestUserConfirm(final String message) {
        return listener.onRequestUserConfirm(message);
    }

    protected int requestUserChoose(final String message, final String[] item) {
        return listener.onRequestUserChoose(message, item);
    }

    protected void showMessage(String format, Object... args) {
        listener.onShowMessage(String.format(format, args));
    }

    public static interface ServiceExecutionListener {
        void onServiceStart(Class serviceClass);

        void onServiceStop(Class serviceClass);

        void onServiceException(Class serviceClass, String reason);

        void onTemplateStop();

        String onRequestUserInput(String msg);

        boolean onRequestUserConfirm(String msg);

        int onRequestUserChoose(String msg, String[] items);

        void onShowMessage(String msg);
    }

    protected class ServiceResolver {
        public <T> T resolveService(Class<T> serviceClass, double timePercent, double costPercent) {
            ServiceReference<T> serviceReference = bundleContext.getServiceReference(serviceClass);
            serviceReferences.push(serviceReference);
            T service = bundleContext.getService(serviceReference);
            initConcreteService(timePercent, costPercent, (ConcreteService) service);
            return new ServiceHandler<T>().newProxyInstance(service);
        }

        private void initConcreteService(double timePercent, double costPercent, ConcreteService concreteService) {
            concreteService.setContext(context);
            //TODO After tfws1 Added
//            TimeCost timeCost = serviceTimeCostMap.get(serviceClass.getName());
//            concreteService.setTime(timeCost.time);
//            concreteService.setCost(timeCost.cost);
            concreteService.setTime((int) (totalTimeCost.time * timePercent));
            concreteService.setCost((int) (totalTimeCost.cost * costPercent));
            concreteService.setTemplateName(getTemplateName());
        }
    }

    private class ServiceHandler<T> implements InvocationHandler {
        private T target;

        public T newProxyInstance(T target) {
            this.target = target;
            return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                    target.getClass().getInterfaces(), this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = null;
            Class targetClass = target.getClass();
            listener.onServiceStart(targetClass);
            try {
                result = method.invoke(target, args);
                listener.onServiceStop(targetClass);
            } catch (Exception e) {
                listener.onServiceException(targetClass, e.getMessage());
                e.printStackTrace();
            }
            return result;
        }
    }

    private class TimeCost {
        final int time, cost;

        public TimeCost(int time, int cost) {
            this.time = time;
            this.cost = cost;
        }
    }
}
