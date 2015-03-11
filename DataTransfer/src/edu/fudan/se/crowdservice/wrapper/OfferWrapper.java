package edu.fudan.se.crowdservice.wrapper;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class OfferWrapper extends Wrapper {
    private static final long serialVersionUID = -7046085935989249258L;

    public final int price;
    public final int time;

    public OfferWrapper(long taskId, int price, int time) {
        super(taskId);
        this.price = price;
        this.time = time;
    }

    @Override
    public String toString() {
        return "OfferWrapper{" +
                "taskId=" + taskId +
                ",price=" + price +
                ",time=" + time +
                '}';
    }
}
