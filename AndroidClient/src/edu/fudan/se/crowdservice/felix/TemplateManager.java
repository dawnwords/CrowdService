package edu.fudan.se.crowdservice.felix;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Binder;
import edu.fudan.se.crowdservice.core.TemplateFactory;
import jade.util.Logger;
import org.osgi.framework.*;
import org.osgi.service.obr.*;

import java.net.URL;
import java.util.Iterator;
import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/30.
 */
public class TemplateManager extends Binder {
    private String templateRepoURL;
    private String serviceRepoUrl;
    private RepositoryAdmin admin;
    private BundleContext bundleContext;
    private Resolver resolver;
    private Logger logger = Logger.getJADELogger(TemplateManager.class.getName());

    public TemplateManager(BundleContext bundleContext, String obrIp, int obrPort) {
        this.bundleContext = bundleContext;
        this.admin = bundleContext.getService(bundleContext.getServiceReference(RepositoryAdmin.class));
        this.templateRepoURL = String.format("http://%s:%d/obr/%s_repo.xml", obrIp, obrPort, "template");
        this.serviceRepoUrl = String.format("http://%s:%d/obr/%s_repo.xml", obrIp, obrPort, "service");
    }

    public void getAvailableTemplateResource(final OnResourcesReceivedListener listener) {
        new AsyncTask<Void, Void, Resource[]>() {
            @Override
            protected Resource[] doInBackground(Void... voids) {
                try {
                    Repository templateRepo = admin.addRepository(new URL(templateRepoURL));
                    admin.addRepository(new URL(serviceRepoUrl));
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

    public void resolveTemplate(Resource resource, final Context context, final OnTemplateResolvedListener listener) {
        new AsyncTask<Resource, Void, TemplateFactory>() {
            @Override
            protected TemplateFactory doInBackground(Resource... resources) {
                Resource resource = resources[0];
                resolver = admin.resolver();
                resolver.add(resource);
                if (resolver.resolve()) {
                    info("Deploying ...");
                    resolver.deploy(true);
                    info("Deploy Successfully!");
                    listBundles();

                    String filter = String.format("(%s=%s)", Constants.BUNDLE_SYMBOLICNAME, resource.getSymbolicName());
                    try {
                        Iterator<ServiceReference<TemplateFactory>> iterator = bundleContext.getServiceReferences(TemplateFactory.class, filter).iterator();
                        if (iterator.hasNext()) {
                            TemplateFactory templateFactory = bundleContext.getService(iterator.next());
                            templateFactory.setContext(context);
                            return templateFactory;
                        }
                    } catch (InvalidSyntaxException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(TemplateFactory templateFactory) {
                if (templateFactory == null) {
                    listener.onFailure(resolver.getUnsatisfiedRequirements());
                } else {
                    listener.onTemplateResolved(templateFactory);
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
        void onTemplateResolved(TemplateFactory templateFactory);

        void onFailure(Requirement[] unsatisfiedRequirements);
    }
}
