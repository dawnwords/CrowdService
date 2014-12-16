package service.implamentation;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import service.interfaces.DivideService;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public class DivideServiceImpl implements DivideService, BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        bundleContext.registerService(DivideService.class.getName(), this, null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
    }

    @Override
    public double div(double a, double b) {
        return a / b;
    }
}
