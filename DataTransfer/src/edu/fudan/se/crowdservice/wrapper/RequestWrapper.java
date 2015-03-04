package edu.fudan.se.crowdservice.wrapper;

/**
 * Created by Jiahuan on 2015/1/23.
 */
public class RequestWrapper extends Wrapper {
    private static final long serialVersionUID = 4252578771672212785L;

    public final String description;

    public RequestWrapper(long taskId, String description) {
        super(taskId);
        this.description = description;
    }

    @Override
    public String toString() {
        return "RequestWrapper{" +
                "taskId=" + taskId + "," +
                "description='" + description + '\'' +
                '}';
    }
}
