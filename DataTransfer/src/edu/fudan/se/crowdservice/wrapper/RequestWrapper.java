package edu.fudan.se.crowdservice.wrapper;

/**
 * Created by Jiahuan on 2015/1/23.
 */
public class RequestWrapper extends Wrapper {
    private static final long serialVersionUID = 4252578771672212785L;

    public final String description;
    public final int deadline;

    public RequestWrapper(long taskId, String description, int deadline) {
        super(taskId);
        this.description = description;
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "RequestWrapper{" +
                "taskId=" + taskId +
                ",deadline=" + deadline +
                ",description='" + description + '\'' +
                '}';
    }
}
