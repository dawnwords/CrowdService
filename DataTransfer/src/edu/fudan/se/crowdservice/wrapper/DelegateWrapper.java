package edu.fudan.se.crowdservice.wrapper;

import edu.fudan.se.crowdservice.kv.KeyValueHolder;

import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/23.
 */
public class DelegateWrapper extends Wrapper {
    public final ArrayList<KeyValueHolder> keyValueHolders;

    public DelegateWrapper(long taskId, ArrayList<KeyValueHolder> keyValueHolders) {
        super(taskId);
        this.keyValueHolders = keyValueHolders;
    }
}
