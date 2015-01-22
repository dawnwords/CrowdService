package edu.fudan.se.crowdservice.jade.agent.behaviour;

import edu.fudan.se.crowdservice.core.Template;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Created by Jiahuan on 2015/1/21.
 */
public class TemplateExecutingBehaviour extends OneShotBehaviour {

    private Template template;

    public TemplateExecutingBehaviour(Template template) {
        this.template = template;
    }

    @Override
    public void action() {
        template.executeTemplate();
    }
}
