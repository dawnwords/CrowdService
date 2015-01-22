package edu.fudan.se.crowdservice.wrapper;

import java.io.Serializable;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class OfferWrapper implements Serializable {
    public final long taskId;
    public final int price;

    public OfferWrapper(long taskId, int price) {
        this.taskId = taskId;
        this.price = price;
    }
}
