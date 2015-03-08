package edu.fudan.se.crowdservice.jade.agent.behaviour;

import android.os.Handler;
import edu.fudan.se.crowdservice.core.*;
import edu.fudan.se.crowdservice.data.ConsumerSession;
import edu.fudan.se.crowdservice.jade.agent.uimessage.ConsumerSessionMessage;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Created by Jiahuan on 2015/1/21.
 */
public class TemplateExecutingBehaviour extends OneShotBehaviour {

    private Template template;
    private ResultHolder<String> result = new ResultHolder<String>();

    public TemplateExecutingBehaviour(int sessionID, TemplateFactory templateFactory, Handler handler) {
        this.template = templateFactory.createTemplateInstance(new Listener(sessionID, handler));
    }

    @Override
    public void action() {
        template.executeTemplate();
    }

    public void setResultInput(String resultInput) {
        result.set(resultInput);
    }

    private class Listener implements ServiceExecutionListener {
        private int sessionID;
        private Handler handler;

        public Listener(int sessionID, Handler handler) {
            this.sessionID = sessionID;
            this.handler = handler;
        }

        @Override
        public void onServiceStart(ConcreteService service, Object[] objects) {
            sendConsumerSessionMessage(ConsumerSession.buildServiceStartMessage(sessionID, service.getClass()));
        }

        @Override
        public void onServiceStop(ConcreteService service) {
            sendConsumerSessionMessage(ConsumerSession.buildServiceStopMessage(sessionID, service.getClass()));
        }

        @Override
        public void onServiceException(ConcreteService service, String reason) {
            sendConsumerSessionMessage(ConsumerSession.buildServiceExceptionMessage(sessionID, service.getClass(), reason));
        }

        @Override
        public void onTemplateStop() {
            sendConsumerSessionMessage(ConsumerSession.buildTemplateStopMessage(sessionID));
        }

        @Override
        public String onRequestUserInput(String hint) {
            sendConsumerSessionMessage(ConsumerSession.buildRequestInputMessage(sessionID, hint));
            String input = result.get();
            sendConsumerSessionMessage(ConsumerSession.buildConsumerInputMessage(sessionID, input));
            return input;
        }

        @Override
        public boolean onRequestUserConfirm(String hint) {
            sendConsumerSessionMessage(ConsumerSession.buildRequestConfirmMessage(sessionID, hint));
            boolean confirm = result.get().equals(Boolean.TRUE.toString());
            sendConsumerSessionMessage(ConsumerSession.buildConsumerInputMessage(sessionID, confirm ? "Yes" : "No"));
            return confirm;
        }

        @Override
        public int onRequestUserChoose(String hint, String[] choices) {
            sendConsumerSessionMessage(ConsumerSession.buildRequestChooseMessage(sessionID, hint, choices));
            int choice = Integer.parseInt(result.get());
            sendConsumerSessionMessage(ConsumerSession.buildConsumerInputMessage(sessionID, choices[choice]));
            return choice;
        }

        @Override
        public void onShowMessage(String content) {
            sendConsumerSessionMessage(ConsumerSession.buildShowMessageMessage(sessionID, content));
        }

        private void sendConsumerSessionMessage(ConsumerSession.Message message) {
            handler.sendMessage(new ConsumerSessionMessage(message).asMessage());
        }
    }
}
