package edu.fudan.se.crowdservice.felix;

import edu.fudan.se.crowdservice.core.Template;
import jade.util.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.obr.*;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Created by Dawnwords on 2014/12/18.
 */
public class TemplateRepo extends Thread {
    private static final HashMap<Integer, String> STATE_STRING_MAP = new HashMap<Integer, String>();

    static {
        STATE_STRING_MAP.put(1, "UNINSTALLED");
        STATE_STRING_MAP.put(2, "INSTALLED");
        STATE_STRING_MAP.put(4, "RESOLVED");
        STATE_STRING_MAP.put(8, "STARTING");
        STATE_STRING_MAP.put(16, "STOPPING");
        STATE_STRING_MAP.put(32, "ACTIVE");
    }

    private BundleContext bundleContext;
    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    public TemplateRepo(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Override
    public void run() {
        try {
            RepositoryAdmin admin = bundleContext.getService(bundleContext.getServiceReference(RepositoryAdmin.class));
            Repository templateRepo = admin.addRepository(new URL("http://10.131.252.156:8080/obr/template_repo.xml"));
            admin.addRepository(new URL("http://10.131.252.156:8080/obr/service_repo.xml"));
            Resolver resolver = admin.resolver();

            info("Repo name is %s", templateRepo.getName());
            info("Repo last modified %s", new Date(templateRepo.getLastModified()));
            info("Repo URL is %s", templateRepo.getURL());
            Resource[] res = templateRepo.getResources();
            if (res != null) {
                for (Resource re : res) {
                    info("id:%s, SymbolicName:%s", re.getId(), re.getSymbolicName());
                    if (re.getSymbolicName().contains("ArithmeticTemplate")) {
                        resolver.add(re);
                    }
                }
            }
            if (resolver.resolve()) {
                info("Deploying ...");
                resolver.deploy(true);
                info("Deploy Successfully!");
                for (Bundle bundle : bundleContext.getBundles()) {
                    info("[%s]Symbolic Name:%s,Location:%s", stateToString(bundle.getState()), bundle.getSymbolicName(), bundle.getLocation());
                }
                Template template = bundleContext.getService(bundleContext.getServiceReference(Template.class));
                template.executeTemplate();
            } else {
                info("Fail for");
                for (Requirement requirement : resolver.getUnsatisfiedRequirements()) {
                    info("Name: %s,Comment: %s,Filter: %s", requirement.getName(), requirement.getComment(), requirement.getFilter());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String stateToString(int state) {
        return STATE_STRING_MAP.get(state);
    }

    private void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    private void info(String format, Object... msg) {
        info(String.format(format + "\n", msg));
    }
}
