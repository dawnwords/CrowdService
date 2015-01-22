package edu.fudan.se.crowdservice.jade.agent.uimessage;


import edu.fudan.se.crowdservice.kv.KeyValueHolder;

import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class DelegateMessage extends UIMessage<ArrayList<KeyValueHolder>> {
    public static final int DELEGATE = 1442;

    public DelegateMessage(ArrayList<KeyValueHolder> value) {
        super(DELEGATE, value);
    }
}
