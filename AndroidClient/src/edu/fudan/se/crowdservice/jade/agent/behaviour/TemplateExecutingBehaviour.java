package edu.fudan.se.crowdservice.jade.agent.behaviour;

import android.os.Handler;
import edu.fudan.se.crowdservice.core.ResultHolder;
import edu.fudan.se.crowdservice.core.Template;
import edu.fudan.se.crowdservice.data.ConsumerSession;
import edu.fudan.se.crowdservice.jade.agent.uimessage.ConsumerSessionMessage;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Created by Jiahuan on 2015/1/21.
 */
public class TemplateExecutingBehaviour extends OneShotBehaviour {

    private Template template;
    private int sessionID;
    private Handler handler;
    private ResultHolder<String> result = new ResultHolder<String>();
    private Template.ServiceExecutionListener listener = new Template.ServiceExecutionListener() {
        @Override
        public void onServiceStart(Class serviceClass) {
            sendConsumerSessionMessage(ConsumerSession.buildServiceStartMessage(sessionID, serviceClass));
        }

        @Override
        public void onServiceStop(Class serviceClass) {
            sendConsumerSessionMessage(ConsumerSession.buildServiceStopMessage(sessionID, serviceClass));
        }

        @Override
        public void onServiceException(Class serviceClass, String reason) {
            sendConsumerSessionMessage(ConsumerSession.buildServiceExceptionMessage(sessionID, serviceClass, reason));
        }

        @Override
        public void onTemplateStop() {
            sendConsumerSessionMessage(ConsumerSession.buildTemplateStopMessage(sessionID));
        }

        @Override
        public String onRequestUserInput(String hint) {
            sendConsumerSessionMessage(ConsumerSession.buildRequestInputMessage(sessionID, hint));
            return result.get();
        }

        @Override
        public boolean onRequestUserConfirm(String hint) {
            sendConsumerSessionMessage(ConsumerSession.buildRequestConfirmMessage(sessionID, hint));
            return result.get().equals(Boolean.TRUE.toString());
        }

        @Override
        public int onRequestUserChoose(String hint, String[] choices) {
            sendConsumerSessionMessage(ConsumerSession.buildRequestChooseMessage(sessionID, hint, choices));
            return Integer.parseInt(result.get());
        }

        @Override
        public void onShowMessage(String content) {
            sendConsumerSessionMessage(ConsumerSession.buildShowMessageMessage(sessionID, content));
        }

        private void sendConsumerSessionMessage(ConsumerSession.Message message) {
            handler.sendMessage(new ConsumerSessionMessage(message).asMessage());
        }
    };

    public TemplateExecutingBehaviour(int sessionID, Template template, Handler handler) {
        this.sessionID = sessionID;
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
