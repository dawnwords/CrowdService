package edu.fudan.se.crowdservice.felix;

import android.os.AsyncTask;
import android.os.Binder;
import edu.fudan.se.crowdservice.core.Template;
import jade.util.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.obr.*;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/30.
 */
public class TemplateManager extends Binder {
    private static final String OBR_REPO_URL = "http://10.131.252.156:8080/obr/${TYPE}_repo.xml";
    private static final String OBR_TEMPLATE_REPO_URL = OBR_REPO_URL.replace("${TYPE}", "template");
    private static final String OBR_SERVICE_REPO_URL = OBR_REPO_URL.replace("${TYPE}", "service");

    private RepositoryAdmin admin;
    private BundleContext bundleContext;
    private ConcurrentHashMap<Template, ServiceReference<Template>> templateRefMap;
    private Resolver resolver;
    private Logger logger = Logger.getJADELogger(TemplateManager.class.getName());
    private Template.OnTemplateStopListener templateStopListener = new Template.OnTemplateStopListener() {
        @Override
        public void onTemplateStop(Template template) {
            Bundle bundle = templateRefMap.remove(template).getBundle();
            info("Uninstall Bundle:%s\n", bundle.getSymbolicName());
            try {
                bundle.uninstall();
            } catch (BundleException e) {
                e.printStackTrace();
            }
            listBundles();
        }
    };

    public TemplateManager(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
        this.templateRefMap = new ConcurrentHashMap<Template, ServiceReference<Template>>();
        this.admin = bundleContext.getService(bundleContext.getServiceReference(RepositoryAdmin.class));
    }

    public void getAvailableTemplateResource(final OnResourcesReceivedListener listener) {
        new AsyncTask<Void, Void, Resource[]>() {
            @Override
            protected Resource[] doInBackground(Void... voids) {
                try {
                    Repository templateRepo = admin.addRepository(new URL(OBR_TEMPLATE_REPO_URL));
                    admin.addRepository(new URL(OBR_SERVICE_REPO_URL));
                    return templateRepo.getResources();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Resource[] resources) {
                if (resources == null) {
                    listener.onFailure();
                } else {
                    listener.onResourcesReceived(resources);
                }
                listBundles();
            }
        }.execute();
    }

    public void resolveTemplate(Resource resource, final OnTemplateResolvedListener listener) {
        new AsyncTask<Resource, Void, Template>() {
            @Override
            protected Template doInBackground(Resource... resources) {
                resolver = admin.resolver();
                resolver.add(resources[0]);
                if (resolver.resolve()) {
                    info("Deploying ...");
                    resolver.deploy(true);
                    info("Deploy Successfully!");
                    listBundles();
                    ServiceReference<Template> serviceReference = bundleContext.getServiceReference(Template.class);
                    Template template = bundleContext.getService(serviceReference);
                    template.setListener(templateStopListener);
                    templateRefMap.put(template, serviceReference);
                    return template;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Template template) {
                if (template == null) {
                    listener.onFailure(resolver.getUnsatisfiedRequirements());
                } else {
                    listener.onTemplateResolved(template);
                }
            }
        }.execute(resource);
    }

    private void listBundles() {
        for (Bundle bundle : bundleContext.getBundles()) {
            info("[%s]Symbolic Name:%s,Location:%s",
                    BundleState.valueOf(bundle.getState()).toString(),
                    bundle.getSymbolicName(), bundle.getLocation());
        }
    }

    private void info(String format, Object... args) {
        logger.log(Level.INFO, String.format(format, args));
    }

    private static enum BundleState {
        UNINSTALLED(1), INSTALLED(2), RESOLVED(4), STARTING(8), STOPPING(16), ACTIVE(32);

        int state;

        BundleState(int state) {
            this.state = state;
        }

        static BundleState valueOf(int value) {
            return values()[(int) (Math.log(value) / Math.log(2))];
        }
    }

    public static interface OnResourcesReceivedListener {
        void onResourcesReceived(Resource[] resources);

        void onFailure();
    }

    public static interface OnTemplateResolvedListener {
        void onTemplateResolved(Template template);

        void onFailure(Requirement[] unsatisfiedRequirements);
    }
}
