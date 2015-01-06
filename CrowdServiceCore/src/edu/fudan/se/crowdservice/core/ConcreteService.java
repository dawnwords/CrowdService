package edu.fudan.se.crowdservice.core;

import android.content.Context;
import android.content.Intent;
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
    private Handler uiHandler;

    private Logger logger = Logger.getJADELogger(ConcreteService.class.getName());

    @Override
    public final void start(BundleContext context) throws Exception {
        String serviceName = getServiceInterface().getName();
        logger.log(Level.INFO, "Register Service:" + serviceName);
        context.registerService(serviceName, this, null);
    }

    @Override
    public final void stop(BundleContext context) throws Exception {
    }

    void setContext(Context context) {
        this.context = context;
    }

    void setUiHandler(Handler uiHandler) {
        this.uiHandler = uiHandler;
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
