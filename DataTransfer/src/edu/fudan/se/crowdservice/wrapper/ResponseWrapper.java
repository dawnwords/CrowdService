package edu.fudan.se.crowdservice.wrapper;

import edu.fudan.se.crowdservice.kv.KeyValueHolder;

import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class ResponseWrapper extends Wrapper {
    public final ArrayList<KeyValueHolder> keyValueHolders;

    public ResponseWrapper(long taskId, ArrayList<KeyValueHolder> keyValueHolders) {
        super(taskId);
        this.keyValueHolders = keyValueHolders;
    }
}
