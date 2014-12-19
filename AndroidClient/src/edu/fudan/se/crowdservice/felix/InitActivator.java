package edu.fudan.se.crowdservice.felix;

import android.content.res.Resources;
import edu.fudan.se.crowdservice.R;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.io.File;

public class InitActivator implements BundleActivator {

    private Resources res;

    public InitActivator(Resources res) {
        this.res = res;
    }

    public void start(BundleContext context) throws Exception {
        startInitBundle(context, R.raw.fileinstall130, "fileinstall130.jar");
        startInitBundle(context, R.raw.obr, "obr.jar");
        startInitBundle(context, R.raw.bundlerepository, "bundlerepository.jar");
        startInitBundle(context, R.raw.shell, "shell.jar");
        startInitBundle(context, R.raw.ipojo, "ipojo.jar");
        startInitBundle(context, R.raw.ipojoannotations, "ipojoannotations.jar");
        startInitBundle(context, R.raw.ipojoarch, "ipojoarch.jar");
    }


    private void startInitBundle(BundleContext context, int rawId, String jarName) throws Exception {
        String bundlePath = Parameter.getInstance().getInitBundleDir().getAbsolutePath()
                + File.separator + jarName;
        Bundle bundle = context.installBundle(bundlePath, res.openRawResource(rawId));
        bundle.start();
    }

    public void stop(BundleContext arg0) throws Exception {
    }
}
