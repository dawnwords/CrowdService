package edu.fudan.se.crowdservice.wrapper;

/**
 * Created by Jiahuan on 2015/1/23.
 */
public class RefuseWrapper extends Wrapper {
    private static final long serialVersionUID = -1774891502313329253L;
    public final String reason;

    public RefuseWrapper(long taskId, Reason reason) {
        super(taskId);
        this.reason = reason.reason;
    }

    public static enum Reason {
        OFFER_NOT_SELECTED("Offer Not Selected"), OFFER_OUT_OF_DATE("Offer Out of Date");

        public final String reason;

        Reason(String reason) {
            this.reason = reason;
        }
    }
}
