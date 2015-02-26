package edu.fudan.se.crowdservice.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dawnwords on 2015/2/26.
 */
public class ConsumerSession implements Serializable {
    public final int sessionID;
    public final String templateName;
    public final List<Message> messages;

    public ConsumerSession(int sessionID, String templateName) {
        this.sessionID = sessionID;
        this.templateName = templateName;
        this.messages = new LinkedList<Message>();
    }

    public static Message buildServiceStartMessage(Class service) {
        return new Message(MessageType.SERVICE_START, service.getSimpleName() + " Started.");
    }

    public static Message buildServiceStopMessage(Class service) {
        return new Message(MessageType.SERVICE_STOP, service.getSimpleName() + " Stopped.");
    }

    public static Message buildServiceExceptionMessage(Class service, String reason) {
        reason = String.format("Exception Occurs in %s for %s. ", service.getSimpleName(), reason);
        return new Message(MessageType.SERVICE_STOP, reason);
    }

    public static Message buildTemplateStopMessage() {
        return new Message(MessageType.TEMPLATE_STOP, "Template Stopped.");
    }

    public static Message buildRequestInputMessage(String hint) {
        return new Message(MessageType.REQUEST_INPUT, hint);
    }

    public static Message buildRequestConfirmMessage(String hint) {
        return new Message(MessageType.REQUEST_CONFIRM, hint);
    }

    public static Message buildRequestChooseMessage(String hint, String[] choices) {
        Message message = new Message(MessageType.REQUEST_CHOOSE, hint);
        message.choices = choices;
        return message;
    }

    public static Message buildShowMessageMessage(String content) {
        return new Message(MessageType.SHOW_MESSAGE, content);
    }

    @Override
    public String toString() {
        return "ConsumerSession{" +
                "sessionID=" + sessionID +
                ", templateName='" + templateName + '\'' +
                ", messages=" + messages +
                '}';
    }

    public static enum MessageType {
        CONSUMER_INPUT, SERVICE_START, SERVICE_STOP, SERVICE_EXCEPTION, TEMPLATE_STOP,
        REQUEST_INPUT, REQUEST_CONFIRM, REQUEST_CHOOSE, SHOW_MESSAGE
    }

    public static class Message implements Serializable {
        public final MessageType type;
        public final String content;
        private String[] choices;

        private Message(MessageType type, String content) {
            this.type = type;
            this.content = content;
        }

        public String[] getChoices() {
            return choices;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "type=" + type +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}
