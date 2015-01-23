package edu.fudan.se.crowdservice.wrapper;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class OfferWrapper extends Wrapper {
    public final int price;

    public OfferWrapper(long taskId, int price) {
        super(taskId);
        this.price = price;
    }
}
