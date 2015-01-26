package edu.fudan.se.crowdservice.core;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import jade.util.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/18.
 */
public abstract class ConcreteService implements BundleActivator {
    protected Context context;
    protected String consumerId;
    protected int time, cost;
    protected String templateName;
    private Handler uiHandler;
    private Logger logger = Logger.getJADELogger(ConcreteService.class.getName());

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        String serviceName = getServiceInterface().getName();
        logger.log(Level.INFO, "Register Service:" + serviceName);
        bundleContext.registerService(serviceName, this, null);
        SharedPreferences setting = context.getSharedPreferences(SavedProperty.CROWD_SERVICE, 0);
        consumerId = setting.getString(SavedProperty.AGENT_NAME, "");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    }

    void setContext(Context context) {
        this.context = context;
    }

    void setUiHandler(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }

    void setTime(int time) {
        this.time = time;
    }

    void setCost(int cost) {
        this.cost = cost;
    }

    void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    protected abstract Class getServiceInterface();

    protected Class<? extends ServiceActivity> getServiceActivity() {
        return null;
    }

    protected void postUIRunnable(Runnable runnable) {
        uiHandler.post(runnable);
    }

    protected void startServiceActivity(final Bundle extraBundle) {
        if (getServiceActivity() != null) {
            postUIRunnable(new Runnable() {
                @Override
                public void run() {
                    try {
                        AdapterActivity.ServiceActivityHolder.setActivity(getServiceActivity().newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(context, AdapterActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra(ServiceActivity.EXTRA_BUNDLE, extraBundle);
                    context.startActivity(intent);
                }
            });
        }
    }

    protected <T> T startServiceActivityForResult(final Bundle extraBundle) {
        ResultHolder<T> resultHolder = new ResultHolder<T>();
        ActivityResult.getInstance().setServiceActivityResultHolder(resultHolder);
        startServiceActivity(extraBundle);
        return resultHolder.get();
    }
}
