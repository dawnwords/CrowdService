package service.shcomputer.cs.judgeworth.impl;

import edu.fudan.se.crowdservice.core.ConcreteService;
import service.shcomputer.cs.judgeworth.interfaces.JudgeWorthOfComputerService;

/**
 * Created by Dawnwords on 2015/1/6.
 */
public class JudgeWorthOfComputerServiceImpl extends ConcreteService implements JudgeWorthOfComputerService {
    @Override
    protected Class getServiceInterface() {
        return JudgeWorthOfComputerService.class;
    }

    @Override
    public boolean judgeWorthOfComputer(String url) {
        return true;
    }
}
