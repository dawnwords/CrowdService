package edu.fudan.se.crowdservice.wrapper;

import edu.fudan.se.crowdservice.kv.KeyValueHolder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class ResponseWrapper extends Wrapper {
    private static final long serialVersionUID = -6270374559450934573L;

    public final ArrayList<KeyValueHolder> keyValueHolders;

    public ResponseWrapper(long taskId, ArrayList<KeyValueHolder> keyValueHolders) {
        super(taskId);
        this.keyValueHolders = keyValueHolders;
    }


    @Override
    public String toString() {
        return "ResponseWrapper{" +
                "taskId=" + taskId + "," +
                "keyValueHolders=" + Arrays.toString(keyValueHolders.toArray()) +
                '}';
    }
}
