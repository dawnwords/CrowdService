package edu.fudan.se.crowdservice.template;

import edu.fudan.se.crowdservice.core.TemplateFactory;

/**
 * Created by Dawnwords on 2015/3/6.
 */
public class BuySHComputerTemplateFactoryV2 extends TemplateFactory<BuySHComputerTemplateV2> {
    @Override
    protected Class<BuySHComputerTemplateV2> getTemplateClass() {
        return BuySHComputerTemplateV2.class;
    }
}
