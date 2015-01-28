package service.ui.impl;

import android.os.Bundle;
import edu.fudan.se.crowdservice.core.ConcreteService;
import edu.fudan.se.crowdservice.core.ServiceActivity;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import service.ui.activity.UIActivity;
import service.ui.interfaces.UIService;

import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/26.
 */
public class UIServiceImpl extends ConcreteService implements UIService {

    @Override
    public void displayUI(ArrayList<KeyValueHolder> crowdServiceResult) {
        if (crowdServiceResult.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(UIActivity.UI_MODEL, crowdServiceResult);
            startServiceActivity(bundle);
        }
    }

    @Override
    protected Class<? extends ServiceActivity> getServiceActivity() {
        return UIActivity.class;
    }

    @Override
    protected Class getServiceInterface() {
        return UIService.class;
    }
}
