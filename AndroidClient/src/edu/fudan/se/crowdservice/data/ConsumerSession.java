package edu.fudan.se.crowdservice.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Dawnwords on 2015/2/26.
 */
public class ConsumerSession implements Serializable {
    private static final SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    public final int sessionID;
    public final String templateName;
    private Date lastMessageTime;
    private ArrayList<Message> messages;

    public ConsumerSession(int sessionID, String templateName) {
        this.sessionID = sessionID;
        this.templateName = templateName;
        this.lastMessageTime = new Date();
        this.messages = new ArrayList<Message>();
    }

    public static Message buildServiceStartMessage(int sessionID, Class service) {
        return new Message(sessionID, MessageType.SERVICE_START, service.getSimpleName() + " Started.");
    }

    public static Message buildServiceStopMessage(int sessionID, Class service) {
        return new Message(sessionID, MessageType.SERVICE_STOP, service.getSimpleName() + " Stopped.");
    }

    public static Message buildServiceExceptionMessage(int sessionID, Class service, String reason) {
        reason = String.format("Exception Occurs in %s for %s. ", service.getSimpleName(), reason);
        return new Message(sessionID, MessageType.SERVICE_STOP, reason);
    }

    public static Message buildTemplateStopMessage(int sessionID) {
        return new Message(sessionID, MessageType.TEMPLATE_STOP, "Template Stopped.");
    }

    public static Message buildRequestInputMessage(int sessionID, String hint) {
        return new Message(sessionID, MessageType.REQUEST_INPUT, hint);
    }

    public static Message buildRequestConfirmMessage(int sessionID, String hint) {
        return new Message(sessionID, MessageType.REQUEST_CONFIRM, hint);
    }

    public static Message buildRequestChooseMessage(int sessionID, String hint, String[] choices) {
        Message message = new Message(sessionID, MessageType.REQUEST_CHOOSE, hint);
        message.choices = choices;
        return message;
    }

    public static Message buildShowMessageMessage(int sessionID, String content) {
        return new Message(sessionID, MessageType.SHOW_MESSAGE, content);
    }

    public static Message buildConsumerInputMessage(int sessionID, String input) {
        return new Message(sessionID, MessageType.CONSUMER_INPUT, input);
    }

    @Override
    public String toString() {
        return "ConsumerSession{" +
                "sessionID=" + sessionID +
                ", templateName='" + templateName + '\'' +
                ", messages=" + Arrays.toString(messages.toArray()) +
                '}';
    }

    public ArrayList<Message> getMessageList() {
        return messages;
    }

    public String time() {
        return format.format(lastMessageTime);
    }

    public void addMessage(Message message) {
        messages.add(message);
        lastMessageTime = new Date();
    }

    public Message getLastMessage() {
        return messages.size() > 0 ? messages.get(messages.size() - 1) : null;
    }

    public static enum MessageType {
        CONSUMER_INPUT, SERVICE_START, SERVICE_STOP, SERVICE_EXCEPTION, TEMPLATE_STOP,
        REQUEST_INPUT, REQUEST_CONFIRM, REQUEST_CHOOSE, SHOW_MESSAGE;

        public static MessageType valueOf(int what) {
            return MessageType.values()[what];
        }
    }

    public static class Message implements Serializable {
        public final int sessionID;
        public final MessageType type;
        public final String content;
        public final String createTime;
        private String[] choices;

        private Message(int sessionID, MessageType type, String content) {
            this.sessionID = sessionID;
            this.type = type;
            this.content = content;
            this.createTime = format.format(new Date());
        }

        public String[] getChoices() {
            return choices;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "sessionID=" + sessionID +
                    ", type=" + type +
                    ", content='" + content + '\'' +
                    ", lastMessageTime='" + createTime + '\'' +
                    (choices == null ? "" : (", choices=" + Arrays.toString(choices))) +
                    '}';
        }
    }
}
