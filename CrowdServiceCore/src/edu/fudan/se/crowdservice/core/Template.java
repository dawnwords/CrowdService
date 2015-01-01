package edu.fudan.se.crowdservice.core;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.text.Editable;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import jade.util.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public abstract class Template implements BundleActivator {

    private BundleContext bundleContext;
    private Context context;
    private Handler uiHandler;
    private OnTemplateStopListener listener;

    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;
        log("Start Template...");
        this.bundleContext.registerService(Template.class, this, null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
    }

    public void setStopListener(OnTemplateStopListener listener) {
        this.listener = listener;
    }

    public void setUiHandler(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void executeTemplate() {
        log("Resolve Service...");
        resolveService(new ServiceResolver());
        log("Execute Template");
        execute();
        log("Stop Template");
        listener.onTemplateStop(this);
    }

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

    protected void showMessage(final String message) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private AlertDialog.Builder getBuilder(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder.setTitle(message).setCancelable(false);
    }

    public static interface OnTemplateStopListener {
        void onTemplateStop(Template template);
    }

    protected class ServiceResolver {
        public <T> T resolveService(Class<T> serviceClass) {
            ServiceReference<T> serviceReference = bundleContext.getServiceReference(serviceClass);
            T service = bundleContext.getService(serviceReference);
            ConcreteService concreteService = (ConcreteService)service;
            concreteService.setContext(context);
            concreteService.setUiHandler(uiHandler);
            return service;
        }
    }
}
