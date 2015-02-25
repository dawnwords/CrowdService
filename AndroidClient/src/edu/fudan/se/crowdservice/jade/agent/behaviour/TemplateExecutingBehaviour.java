package edu.fudan.se.crowdservice.jade.agent.behaviour;

import android.os.Handler;
import edu.fudan.se.crowdservice.core.ResultHolder;
import edu.fudan.se.crowdservice.core.Template;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Created by Jiahuan on 2015/1/21.
 */
public class TemplateExecutingBehaviour extends OneShotBehaviour {

    private Template template;
    private Handler handler;
    private ResultHolder<String> result = new ResultHolder<String>();
    private Template.ServiceExecutionListener listener = new Template.ServiceExecutionListener() {
        @Override
        public void onServiceStart(Class serviceClass) {
            //TODO post message for handler
        }

        @Override
        public void onServiceStop(Class serviceClass) {
            //TODO post message for handler
        }

        @Override
        public void onServiceException(Class serviceClass, String reason) {
            //TODO post message for handler
        }

        @Override
        public void onTemplateStop() {
            //TODO post message for handler
        }

        @Override
        public String onRequestUserInput(String message) {
            //TODO post message for handler
            return result.get();
        }

        @Override
        public boolean onRequestUserConfirm(String s) {
            //TODO post message for handler
            return result.get().equals(Boolean.TRUE.toString());
        }

        @Override
        public int onRequestUserChoose(String s, String[] strings) {
            //TODO post message for handler
            return Integer.parseInt(result.get());
        }

        @Override
        public void onShowMessage(String s) {
            //TODO post message for handler
        }
    };

    public TemplateExecutingBehaviour(Template template, Handler handler) {
        this.template = template;
        this.handler = handler;
    }

    @Override
    public void action() {
        template.executeTemplate(listener);
    }

    public void setResultInput(String resultInput) {
        result.set(resultInput);
    }
}
