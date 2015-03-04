package edu.fudan.se.crowdservice.wrapper;

/**
 * Created by Dawnwords on 2015/3/3.
 */
public class CompleteWrapper extends Wrapper {
    private static final long serialVersionUID = -8672514790738845223L;

    public CompleteWrapper(long taskId) {
        super(taskId);
    }

    @Override
    public String toString() {
        return "CompleteWrapper{" +
                "taskId=" + taskId + "," +
                "}";
    }
}
