package edu.fudan.se.crowdservice.core;

import jade.util.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public abstract class Template {

    private ServiceExecutionListener listener;
    private TemplateFactory.ServiceResolver resolver;
    private TemplateFactory.InstanceCount instanceCount;
    private Map<String, Integer> resultNums;
    private List<String> serviceSequence;

    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    void setInstanceCount(TemplateFactory.InstanceCount instanceCount) {
        this.instanceCount = instanceCount;
    }

    void setServiceExecutionListener(ServiceExecutionListener listener) {
        this.listener = new mServiceExecutionListener(listener);
    }

    void setServiceResolver(TemplateFactory.ServiceResolver resolver) {
        this.resolver = resolver;
    }

    public void executeTemplate() {
        log("Resolve Service...");
        ServiceResolver resolver = new ServiceResolver();
        resolveService(resolver);
        requestTotalTimeCost();
        log("Execute Template");
        execute();
        log("Stop Template");
        listener.onTemplateStop();
    }

    private void requestTotalTimeCost() {
        double reliability;
        do {
            int time = intInput("Please input expected execution time for this template:(s)");
            int cost = intInput("Please input expected cost for this template:(￠)");
            showMessage("Given Execution Time:%d(s), Cost:%d￠, Assessing Reliability...", time, cost);
            reliability = assessReliability(time, cost);
        } while (requestUserConfirm(String.format("Reliability: %.2f%%. Reinput time and cost?", reliability)));
    }

    private int intInput(String msg) {
        do {
            try {
                return Integer.parseInt(requestUserInput(msg));
            } catch (Exception e) {
                showMessage("An integer is required");
            }
        } while (true);
    }

    private double assessReliability(int time, int cost) {
        //TODO invoke tfws1
        return 0;
    }

    private void planTimeCost(ConcreteService service) {
        // TODO invoke tfws2
        service.time = 30;
        service.cost = 30;
        service.resultNum = resultNums.get(service.getClass().getName());
    }

    protected abstract void resolveService(ServiceResolver serviceResolver);

    protected abstract void execute();

    protected void log(String msg) {
        logger.log(Level.INFO, msg);
    }

    protected String requestUserInput(String format, Object... args) {
        return listener.onRequestUserInput(String.format(format, args));
    }

    protected boolean requestUserConfirm(String format, Object... args) {
        return listener.onRequestUserConfirm(String.format(format, args));
    }

    protected int requestUserChoose(String format, final String[] item, Object... args) {
        return listener.onRequestUserChoose(String.format(format, args), item);
    }

    protected void showMessage(String format, Object... args) {
        listener.onShowMessage(String.format(format, args));
    }

    protected class ServiceResolver {
        public <S> S resolveService(Class<S> serviceClass) {
            ConcreteService service = (ConcreteService) resolver.resolveService(serviceClass);
            if (service.isCrowd()) {
                int resultNum = intInput(service.getServiceInterfacesName()
                        + " binds a CrowdService.\nPlease input expected Result Number for this Service:");
                resultNums.put(service.getClass().getName(), resultNum);
            }
            return new ServiceHandler<S>().newProxyInstance((S) service);
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
            ConcreteService service = (ConcreteService) target;
            listener.onServiceStart(service, args);
            try {
                result = method.invoke(target, args);
                listener.onServiceStop(service);
            } catch (Exception e) {
                listener.onServiceException(service, e.getMessage());
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

    private class mServiceExecutionListener implements ServiceExecutionListener {

        private ServiceExecutionListener listener;

        public mServiceExecutionListener(ServiceExecutionListener listener) {
            this.listener = listener;
        }

        @Override
        public void onServiceStart(ConcreteService service, Object[] args) {
            listener.onServiceStart(service, args);
            if (service.isCrowd()) {
                int latitude = service.latitudeArgIndex();
                int longitude = service.longitudeArgIndex();
                if (latitude > 0 && latitude < args.length && longitude > 0 && longitude < args.length) {
                    service.longitude = (Long) args[longitude];
                    service.latitude = (Long) args[latitude];
                }
                String interfaceName = service.getServiceInterfacesName();
                onShowMessage(interfaceName + " binds a CrowdService. Planning ");
                planTimeCost(service);
            }
        }

        @Override
        public void onServiceStop(ConcreteService service) {
            listener.onServiceStop(service);
            serviceSequence.add(service.getClass().getName());
        }

        @Override
        public void onServiceException(ConcreteService service, String reason) {
            listener.onServiceException(service, reason);
        }

        @Override
        public void onTemplateStop() {
            listener.onTemplateStop();
            instanceCount.destroyInstance();
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
