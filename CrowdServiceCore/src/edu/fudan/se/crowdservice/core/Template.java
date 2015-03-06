package edu.fudan.se.crowdservice.core;

import jade.util.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public abstract class Template {

    private ServiceExecutionListener listener;
    private TemplateFactory.ServiceResolver resolver;
    private HashMap<String, TimeCost> serviceTimeCostMap;

    private Logger logger = Logger.getJADELogger(this.getClass().getName());
    private TimeCost totalTimeCost;

    void setServiceExecutionListener(ServiceExecutionListener listener) {
        this.listener = listener;
    }

    void setServiceResolver(TemplateFactory.ServiceResolver resolver) {
        this.resolver = resolver;
    }

    public void executeTemplate() {
        this.totalTimeCost = requestTotalTimeCost();
        log("Total Time=" + totalTimeCost.time + ",Cost=" + totalTimeCost.cost);
        planTotalTimeCost(getClass().getSimpleName(), totalTimeCost);
        log("Resolve Service...");
        resolveService(new ServiceResolver());
        log("Execute Template");
        execute();
        log("Stop Template");
        listener.onTemplateStop();
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

    protected class ServiceResolver {
        public <S> S resolveService(Class<S> serviceClass, double timePercent, double costPercent) {
            S service = (S) resolver.resolveService(serviceClass);
            initConcreteService((ConcreteService) service, timePercent, costPercent);
            return new ServiceHandler<S>().newProxyInstance(service);
        }

        private void initConcreteService(ConcreteService service, double timePercent, double costPercent) {
            //TODO After tfws1 Added
//            TimeCost timeCost = serviceTimeCostMap.get(serviceClass.getName());
//            concreteService.setTime(timeCost.time);
//            concreteService.setCost(timeCost.cost);
            service.setTime((int) (totalTimeCost.time * timePercent));
            service.setCost((int) (totalTimeCost.cost * costPercent));
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
        public synchronized Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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
