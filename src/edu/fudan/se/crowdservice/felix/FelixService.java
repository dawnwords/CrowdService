package edu.fudan.se.crowdservice.felix;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import edu.fudan.se.crowdservice.core.AbstractService;
import edu.fudan.se.crowdservice.core.Template;
import jade.util.Logger;
import org.apache.felix.framework.Felix;
import org.osgi.framework.*;
import org.osgi.service.obr.*;
import service.interfaces.AddService;
import service.interfaces.DivideService;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;

public class FelixService extends Service {

    private Felix felix;
    private ServiceInjector serviceInjector;
    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    @Override
    public void onCreate() {
        super.onCreate();

        createInitBundleDir();
        createInstallBundleDir();
        createFelixCacheDir();
        createTemplateDir();
        createOptimizedDir();

        felix = new Felix(new FelixConfig(this));
//        serviceInjector = new ServiceInjector(felix.getBundleContext());
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        shutdownApplication();
    }

    @Override
    public IBinder onBind(Intent intent) {
        try {
            felix.start();
            final BundleContext bundleContext = felix.getBundleContext();
            bundleContext.addFrameworkListener(new FrameworkListener() {
                @Override
                public void frameworkEvent(FrameworkEvent e) {
                    switch (e.getType()) {
                        case FrameworkEvent.STOPPED:
                            felix = null;
                            break;
                        case FrameworkEvent.STARTED:
                            info("Felix has Started!");
                            break;
                    }
                    info("Framework Event " + e.getType());
                }
            });
            bundleContext.addBundleListener(new BundleListener() {
                @Override
                public void bundleChanged(BundleEvent bundleEvent) {
                    Bundle bundle = bundleEvent.getBundle();
                    switch (bundleEvent.getType()) {
                        case BundleEvent.STARTED:
                            String bundleName = new File(bundle.getLocation()).getName();
                            info("Bundle:" + bundleName + " has started!");
//                      BundleContext bundleContext = bundle.getBundleContext();
//                      AbstractService abstractService = (AbstractService) bundleContext.getService(bundle.getRegisteredServices()[0]);
//                      serviceInjector.getServiceListener(bundleName).onServiceStart(abstractService);
                            break;
                        case BundleEvent.STOPPED:
                            try {
                                bundle.uninstall();
                            } catch (BundleException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    info("Bundle Event " + bundleEvent.getType());
                }
            });
            new Thread() {
                @Override
                public void run() {
                    try {
                        RepositoryAdmin admin = bundleContext.getService(bundleContext.getServiceReference(RepositoryAdmin.class));
                        Repository repo = admin.addRepository(new URL("http://10.131.252.156:8080/obr/template_repo.xml"));
                        Resolver resolver = admin.resolver();

                        info("Repo name is " + repo.getName());
                        info("Repo last modified " + new Date(repo.getLastModified()));
                        info("Repo URL is " + repo.getURL());
                        Resource[] res = repo.getResources();
                        if (res != null) {
                            for (Resource re : res) {
                                info("id:" + re.getId());
                                info("" + re.getSymbolicName());
                                if ("service.temlate.ArithmeticTemplate".equals(re.getSymbolicName())) {
                                    resolver.add(re);
                                }
                            }
                        }
                        if (resolver.resolve()) {
                            info("Deploying ...");
                            resolver.deploy(true);
                            info("Deploy Successfully!");
                            sleep(3000);
                            Template template = bundleContext.getService(bundleContext.getServiceReference(Template.class));
                            for(Field field:template.getClass().getFields()){
                                String fieldName = field.getClass().getName();
                                field.setAccessible(true);
                                if("service.interface.AddService".equals(fieldName)){
                                    field.set(template,new AddService() {
                                        @Override
                                        public int add(int a, int b) {
                                            return a+b;
                                        }
                                    });
                                }else if("service.interface.DivideService".equals(fieldName)){
                                    field.set(template,new DivideService() {
                                        @Override
                                        public double div(double a, double b) {
                                            return a/b;
                                        }
                                    });
                                }
                            }
                            template.execute();

                        } else {
                            info("Fail for");
                            for (Requirement requirement : resolver.getUnsatisfiedRequirements()) {
                                info(requirement.toString());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();


        } catch (Exception ex) {
            logger.log(Level.INFO, "Could not create framework: " + ex.getMessage());
            ex.printStackTrace();
        }

        return serviceInjector;
    }

    private void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    private void shutdownApplication() {
        try {
            felix.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createInitBundleDir() {
        Parameter.getInstance().setInitBundleDir(createClearDir("bundle"));
    }

    private void createInstallBundleDir() {
        Parameter.getInstance().setNewBundleDir(createClearDir("newbundle"));
    }

    private void createFelixCacheDir() {
        createClearDir("cache");
    }

    private void createTemplateDir() {
        Parameter.getInstance().setTemplateDir(createClearDir("template"));
    }

    private void createOptimizedDir() {
        Parameter.getInstance().setOptimizedDir(createClearDir("optimized"));
    }

    private File createClearDir(String name) {
        File dir = new File(getRootPath() + File.separator + "felix" + File.separator + name);
        if (dir.exists()) {
            this.delete(dir);
        }
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("Unable to create " + name + " dir");
        }
        return dir;
    }

    private String getRootPath() {
        return getFilesDir().getAbsolutePath();
    }

    private void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null)
                for (File f : files) {
                    delete(f);
                }
        }
        file.delete();
    }

}