package edu.fudan.se.crowdservice.core;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import jade.util.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
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
        this.uiHandler = new Handler(Looper.getMainLooper());
        logger.log(Level.INFO, "Register Service:" + serviceName);
        bundleContext.registerService(serviceName, this, null);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    }

    void setContext(Context context) {
        this.context = context;
        this.consumerId = context.getSharedPreferences(SavedProperty.CROWD_SERVICE, 0).getString(SavedProperty.AGENT_NAME, "");
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

    protected <T> T startServiceActivity(final Bundle extraBundle) {
        if (getServiceActivity() != null) {
            ResultHolder<T> resultHolder = new ResultHolder<T>();
            ActivityResult.getInstance().setServiceActivityResultHolder(resultHolder);
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
            return resultHolder.get();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    protected String loadDataAsBase64ByPath(String imagePath) {
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;
        String result = null;
        try {
            out = new ByteArrayOutputStream();
            in = new BufferedInputStream(context.openFileInput(imagePath));
            byte[] temp = new byte[4096];
            int size;
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            result = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(out);
            close(in);
        }
        return result;
    }

    protected void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
