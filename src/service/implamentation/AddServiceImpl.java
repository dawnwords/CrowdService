package service.implamentation;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import service.interfaces.AddService;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public class AddServiceImpl implements BundleActivator, AddService {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        bundleContext.registerService(AddService.class.getName(), this, null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
    }
}
