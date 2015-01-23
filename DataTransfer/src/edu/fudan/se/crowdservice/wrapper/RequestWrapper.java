package edu.fudan.se.crowdservice.wrapper;

/**
 * Created by Jiahuan on 2015/1/23.
 */
public class RequestWrapper extends Wrapper {
    public final String description;

    public RequestWrapper(long taskId, String description) {
        super(taskId);
        this.description = description;
    }
}
