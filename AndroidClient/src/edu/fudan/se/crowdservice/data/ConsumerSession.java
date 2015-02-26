package edu.fudan.se.crowdservice.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dawnwords on 2015/2/26.
 */
public class ConsumerSession implements Serializable {
    private static final long serialVersionUID = -2977673584125757775L;
    public final int sessionID;
    public final String templateName;
    public final List<Message> messages;

    public ConsumerSession(int sessionID, String templateName) {
        this.sessionID = sessionID;
        this.templateName = templateName;
        this.messages = new LinkedList<Message>();
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
        CONSUMER_INPUT, TEMPLATE_STATE, REQUEST_INPUT, REQUEST_CONFIRM, REQUEST_CHOOSE, SHOW_MESSAGE
    }

    public static class Message implements Serializable {
        private static final long serialVersionUID = -193459147791258672L;
        public final MessageType type;
        public final String content;

        public Message(MessageType type, String content) {
            this.type = type;
            this.content = content;
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
