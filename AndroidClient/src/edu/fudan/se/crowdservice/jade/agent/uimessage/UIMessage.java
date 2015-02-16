package edu.fudan.se.crowdservice.jade.agent.uimessage;

import android.os.Bundle;
import android.os.Message;

import java.io.Serializable;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class UIMessage<T extends Serializable> {
    private static final String BUNDLE = "bundle";
    private Message message;

    protected UIMessage(int what, T value) {
        message = new Message();
        message.what = what;
        Bundle bundle = new Bundle();
        bundle.putSerializable("bundle", value);
        message.setData(bundle);
    }

    public UIMessage(Message message) {
        this.message = message;
    }

    public int what() {
        return message.what;
    }

    public Message asMessage() {
        return message;
    }

    public T getValue() {
        return (T) message.getData().getSerializable(BUNDLE);
    }
}
