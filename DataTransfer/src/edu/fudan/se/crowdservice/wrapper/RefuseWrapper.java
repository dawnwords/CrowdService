package edu.fudan.se.crowdservice.wrapper;

/**
 * Created by Jiahuan on 2015/1/23.
 */
public class RefuseWrapper extends Wrapper {
    private static final long serialVersionUID = -1774891502313329253L;

    public final String reason;

    public RefuseWrapper(long taskId, String reason) {
        super(taskId);
        this.reason = reason;
    }
}
