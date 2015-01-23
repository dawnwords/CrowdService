package edu.fudan.se.crowdservice.wrapper;

import edu.fudan.se.crowdservice.kv.KeyValueHolder;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class ResponseWrapper implements Serializable {
    public final long taskId;
    public final ArrayList<KeyValueHolder> keyValueHolders;

    public ResponseWrapper(long taskId, ArrayList<KeyValueHolder> keyValueHolders) {
        this.taskId = taskId;
        this.keyValueHolders = keyValueHolders;
    }
}
