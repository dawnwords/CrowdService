package edu.fudan.se.crowdservice.template;

import edu.fudan.se.crowdservice.core.TemplateFactory;

/**
 * Created by Dawnwords on 2015/3/6.
 */
public class BuySHComputerTemplateFactory extends TemplateFactory<BuySHComputerTemplate> {
    @Override
    protected Class<BuySHComputerTemplate> getTemplateClass() {
        return BuySHComputerTemplate.class;
    }
}
