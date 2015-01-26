package edu.fudan.se.crowdservice.wrapper;

import java.io.Serializable;

/**
 * Created by Jiahuan on 2015/1/23.
 */
public class Wrapper implements Serializable {
    private static final long serialVersionUID = -520096346707738595L;

    public final long taskId;

    public Wrapper(long taskId) {
        this.taskId = taskId;
    }
}
