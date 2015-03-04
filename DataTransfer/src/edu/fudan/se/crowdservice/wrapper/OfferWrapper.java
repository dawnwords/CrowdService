package edu.fudan.se.crowdservice.wrapper;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class OfferWrapper extends Wrapper {
    private static final long serialVersionUID = -7046085935989249258L;

    public final int price;

    public OfferWrapper(long taskId, int price) {
        super(taskId);
        this.price = price;
    }

    @Override
    public String toString() {
        return "OfferWrapper{" +
                "taskId=" + taskId + "," +
                "price=" + price +
                '}';
    }
}
