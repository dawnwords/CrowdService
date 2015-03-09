package edu.fudan.se.crowdservice.core;

import com.google.gson.Gson;
import edu.fudan.se.crowdservice.plan.Request;
import edu.fudan.se.crowdservice.plan.Response;
import jade.util.Logger;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public abstract class Template {

    public static final String PLAN_SERVICE_IP = "10.131.253.211";
    public static final int PLAN_SERVICE_PORT = 8885;
    private static final String PLAN_SERVICE_URL = String.format("http://%s:%d/globaloptimization?wsdl", PLAN_SERVICE_IP, PLAN_SERVICE_PORT);
    private static final String PLAN_SERVICE_NAMESPACE = "http://ws.sutd.edu.sg/";
    private static final String PLAN_SERVICE_METHOD = "globalOptimize";


    private ServiceExecutionListener listener;
    private TemplateFactory.ServiceResolver resolver;
    private TemplateFactory.InstanceCount instanceCount;
    private Map<String, Integer> resultNums;
    private List<String> serviceSequence;
    private long deadline;
    private int costRemain;
    private String consumerId;

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

    void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public void executeTemplate() {
        resultNums = new HashMap<String, Integer>();
        serviceSequence = new LinkedList<String>();
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
        int time, cost;
        do {
            time = intInput("Please input expected execution time for this template:(s)");
            cost = intInput("Please input expected cost for this template:(￠)");
            showMessage("Given Execution Time:%d(s), Cost:%d￠, Assessing Reliability...", time, cost);
            reliability = assessReliability(time, cost);
        } while (requestUserConfirm("Reliability: %.2f%%. Reinput time and cost?", reliability * 100));
        setDeadline(time);
        this.costRemain = cost;
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
        Request request = new Request();
        request.setGlobalTime(time);
        request.setGlobalCost(cost);

        Response response = invokePlanWS(request);
        return response == null ? -1 : response.getGlobalReliability();
    }

    private void planTimeCost(ConcreteService service) {
        Request request = new Request();
        request.setGlobalTime(getTimeRemain());
        request.setGlobalCost(costRemain);

        Response response = invokePlanWS(request);
        if (response != null) {
            service.time = response.getTime();
            service.cost = response.getCost();
            service.resultNum = resultNums.get(service.getServiceInterfacesName());
        }
    }

    private void setDeadline(int time) {
        this.deadline = new Date().getTime() + time * 1000;
    }

    private int getTimeRemain() {
        return (int) ((deadline - new Date().getTime()) / 1000);
    }

    private Response invokePlanWS(Request request) {
        request.setResultNumbers(resultNums);
        request.setConsumerId(consumerId);
        request.setTemplateName(getClass().getSimpleName());
        request.setServiceSequence(serviceSequence.toArray(new String[serviceSequence.size()]));

        try {
            Gson gson = new Gson();
            SoapObject soapObject = new SoapObject(PLAN_SERVICE_NAMESPACE, PLAN_SERVICE_METHOD);
            soapObject.addProperty("arg0", gson.toJson(request));
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = false;
            envelope.bodyOut = soapObject;
            new HttpTransportSE(PLAN_SERVICE_URL, 10 * 1000).call(null, envelope);
            if (envelope.getResponse() != null) {
                SoapObject obj = (SoapObject) envelope.bodyIn;
                String response = obj.getPropertyAsString(0);
                if (response != null && !"".equals(response)) {
                    return gson.fromJson(response, Response.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
                resultNums.put(service.getServiceInterfacesName(), resultNum);
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
                    service.longitude = (Double) args[longitude];
                    service.latitude = (Double) args[latitude];
                }
                onShowMessage(service.getServiceInterfacesName() + " binds a CrowdService. Planning ");
                planTimeCost(service);
            }
        }

        @Override
        public void onServiceStop(ConcreteService service) {
            listener.onServiceStop(service);
            costRemain -= service.cost;
            serviceSequence.add(service.getServiceInterfacesName());
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
