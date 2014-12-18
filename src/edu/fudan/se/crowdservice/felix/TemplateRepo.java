package edu.fudan.se.crowdservice.felix;

import edu.fudan.se.crowdservice.core.Template;
import jade.util.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.obr.*;
import service.interfaces.AddService;
import service.interfaces.DivideService;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/18.
 */
public class TemplateRepo extends Thread {
    private BundleContext bundleContext;
    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    public TemplateRepo(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

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
                    if ("service.template.ArithmeticTemplate".equals(re.getSymbolicName())) {
                        resolver.add(re);
                    }
                }
            }
            if (resolver.resolve()) {
                info("Deploying ...");
                resolver.deploy(true);
                info("Deploy Successfully!");
                sleep(3000);
                for (Bundle bundle : bundleContext.getBundles()) {
                    info("Symbolic Name:" + bundle.getSymbolicName() + ",Location:" + bundle.getLocation());
                }
                Template template = bundleContext.getService(bundleContext.getServiceReference(Template.class));
                injectService(template);
            } else {
                info("Fail for");
                for (Requirement requirement : resolver.getUnsatisfiedRequirements()) {
                    info("Name:" + requirement.getName() + ",Comment:" + requirement.getComment() + ",Filter:" + requirement.getFilter());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void injectService(Template template) throws IllegalAccessException {
        for (Field field : template.getClass().getFields()) {
            String fieldName = field.getClass().getName();
            field.setAccessible(true);
            if ("service.interface.AddService".equals(fieldName)) {
                field.set(template, new AddService() {
                    @Override
                    public int add(int a, int b) {
                        return a + b;
                    }
                });
            } else if ("service.interface.DivideService".equals(fieldName)) {
                field.set(template, new DivideService() {
                    @Override
                    public double div(double a, double b) {
                        return a / b;
                    }
                });
            }
        }
        template.execute();
    }

    private void info(String msg) {
        logger.log(Level.INFO, msg);
    }
}
