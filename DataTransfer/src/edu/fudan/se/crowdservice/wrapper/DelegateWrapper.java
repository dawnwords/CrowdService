package edu.fudan.se.crowdservice.wrapper;

import edu.fudan.se.crowdservice.kv.KeyValueHolder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jiahuan on 2015/1/23.
 */
public class DelegateWrapper extends Wrapper {
    private static final long serialVersionUID = 5024381597298577179L;

    public final ArrayList<KeyValueHolder> keyValueHolders;
    public final int deadline;
    public final int cost;

    public DelegateWrapper(long taskId, ArrayList<KeyValueHolder> keyValueHolders, int deadline, int cost) {
        super(taskId);
        this.keyValueHolders = keyValueHolders;
        this.deadline = deadline;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "DelegateWrapper{" +
                "taskId=" + taskId +
                ", deadline=" + deadline +
                ", cost=" + cost +
                ", keyValueHolders=" + Arrays.toString(keyValueHolders.toArray()) +
                '}';
    }
}
