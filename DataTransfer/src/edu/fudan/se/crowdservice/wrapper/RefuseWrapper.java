package edu.fudan.se.crowdservice.wrapper;

/**
 * Created by Jiahuan on 2015/1/23.
 */
public class RefuseWrapper extends Wrapper {
    public final String reason;

    public RefuseWrapper(long taskId, String reason) {
        super(taskId);
        this.reason = reason;
    }
}
