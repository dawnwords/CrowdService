package edu.fudan.se.crowdservice.template;

import edu.fudan.se.crowdservice.core.TemplateFactory;

/**
 * Created by Dawnwords on 2015/3/6.
 */
public class ArithmeticTemplateFactory extends TemplateFactory<ArithmeticTemplate> {
    @Override
    protected Class<ArithmeticTemplate> getTemplateClass() {
        return ArithmeticTemplate.class;
    }
}
