package edu.fudan.se.crowdservice.core;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.text.Editable;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
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
    private Handler uiHandler;
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

    public void setUiHandler(Handler uiHandler) {
        this.uiHandler = uiHandler;
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
        final ResultHolder<TimeCost> result = new ResultHolder<TimeCost>();
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout wrapper = new LinearLayout(context);
                wrapper.setOrientation(LinearLayout.VERTICAL);
                LinearLayout timeWrapper = new LinearLayout(context);
                timeWrapper.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout costWrapper = new LinearLayout(context);
                costWrapper.setOrientation(LinearLayout.HORIZONTAL);
                TextView timeText = new TextView(context);
                timeText.setText("Total Time:");
                TextView costText = new TextView(context);
                costText.setText("Total Cost:");
                final EditText timeInput = new EditText(context);
                final EditText costInput = new EditText(context);
                timeInput.setKeyListener(new DigitsKeyListener());
                costInput.setKeyListener(new DigitsKeyListener());

                LayoutParams leftParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3);
                LayoutParams rightParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 7);
                LayoutParams matchParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                timeWrapper.addView(timeText, leftParams);
                timeWrapper.addView(timeInput, rightParams);
                costWrapper.addView(costText, leftParams);
                costWrapper.addView(costInput, rightParams);
                wrapper.addView(timeWrapper, matchParams);
                wrapper.addView(costWrapper, matchParams);

                getBuilder("Please input total time & cost for this template").setView(wrapper)
                        .setPositiveButton("OK", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                result.set(new TimeCost(getIntFromEditText(timeInput), getIntFromEditText(costInput)));
                            }

                            int getIntFromEditText(EditText text) {
                                Editable e = text.getText();
                                return e == null || "".equals(e.toString()) ? 0 : Integer.parseInt(e.toString());
                            }
                        }).create().show();
            }
        });
        return result.get();
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
        final ResultHolder<String> result = new ResultHolder<String>();
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                final EditText input = new EditText(context);
                input.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                getBuilder(message).setView(input)
                        .setPositiveButton("OK", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Editable text = input.getText();
                                result.set(text == null ? "" : text.toString());
                            }
                        }).create().show();
            }
        });
        return result.get();
    }

    protected boolean requestUserConfirm(final String message) {
        final ResultHolder<Boolean> result = new ResultHolder<Boolean>();
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView messageTextView = new TextView(context);
                messageTextView.setText(message);
                getBuilder("Confirm").setView(messageTextView)
                        .setPositiveButton("OK", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.set(true);
                            }
                        })
                        .setNegativeButton("Cancel", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.set(false);
                            }
                        })
                        .create().show();
            }
        });
        return result.get();
    }

    protected int requestUserChoose(final String message, final String[] item) {
        final ResultHolder<Integer> result = new ResultHolder<Integer>();
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                getBuilder(message).setItems(item, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.set(which);
                    }
                }).create().show();
            }
        });
        return result.get();
    }

    protected void showMessage(String format, Object... args) {
        final String message = String.format(format, args);
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private AlertDialog.Builder getBuilder(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder.setTitle(message).setCancelable(false);
    }

    public static interface ServiceExecutionListener {
        void onServiceStart(Class serviceClass);

        void onServiceStop(Class serviceClass);

        void onServiceException(Class serviceClass, String reason);

        void onTemplateStop();
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
            concreteService.setUiHandler(uiHandler);
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
