package service.implamentation;

import edu.fudan.se.crowdservice.core.ConcreteService;
import service.interfaces.AddService;

/**
 * Created by Dawnwords on 2014/12/16.
 */
public class AddServiceImpl extends ConcreteService implements AddService {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    protected Class getServiceInterface() {
        return AddService.class;
    }
}
