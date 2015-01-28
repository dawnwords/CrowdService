package service.shcomputer.app.itemselection.impl;

import edu.fudan.se.crowdservice.core.ConcreteService;
import edu.fudan.se.crowdservice.core.ServiceActivity;
import service.shcomputer.app.itemselection.activity.SHComputerInfoActivity;
import service.shcomputer.app.itemselection.interfaces.SHComputerInfo;
import service.shcomputer.app.itemselection.interfaces.ItemSelectionService;

/**
 * Created by Jiahuan on 2015/1/27.
 */
public class ItemSelectionServiceImpl extends ConcreteService implements ItemSelectionService {
    @Override
    public SHComputerInfo selectItem() {
        return startServiceActivity(null);
    }

    @Override
    protected Class getServiceInterface() {
        return ItemSelectionService.class;
    }

    @Override
    protected Class<? extends ServiceActivity> getServiceActivity() {
        return SHComputerInfoActivity.class;
    }
}
