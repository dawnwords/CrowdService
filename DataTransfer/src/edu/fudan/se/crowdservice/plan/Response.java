package edu.fudan.se.crowdservice.plan;

/**
 * Created by Dawnwords on 2015/3/8.
 */
public class Response {
    private double globalReliability;
    private int time;
    private int cost;

    public double getGlobalReliability() {
        return globalReliability;
    }

    public void setGlobalReliability(double globalReliability) {
        this.globalReliability = globalReliability;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Response{" +
                "globalReliability=" + globalReliability +
                ", time=" + time +
                ", cost=" + cost +
                '}';
    }
}
