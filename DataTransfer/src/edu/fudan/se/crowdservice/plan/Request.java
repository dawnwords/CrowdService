package edu.fudan.se.crowdservice.plan;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by Dawnwords on 2015/3/8.
 */
public class Request {
    private int globalTime;
    private int globalCost;
    private Map<String, Integer> resultNumbers;
    private String templateName;
    private String[] serviceSequence;
    private String consumerId;
    private double targetLongitude, targetLatitude;

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String[] getServiceSequence() {
        return serviceSequence;
    }

    public void setServiceSequence(String[] serviceSequence) {
        this.serviceSequence = serviceSequence;
    }

    public int getGlobalTime() {
        return globalTime;
    }

    public void setGlobalTime(int globalTime) {
        this.globalTime = globalTime;
    }

    public int getGlobalCost() {
        return globalCost;
    }

    public void setGlobalCost(int globalCost) {
        this.globalCost = globalCost;
    }

    public Map<String, Integer> getResultNumbers() {
        return resultNumbers;
    }

    public void setResultNumbers(Map<String, Integer> resultNumbers) {
        this.resultNumbers = resultNumbers;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public double getTargetLongitude() {
        return targetLongitude;
    }

    public void setTargetLongitude(double targetLongitude) {
        this.targetLongitude = targetLongitude;
    }

    public double getTargetLatitude() {
        return targetLatitude;
    }

    public void setTargetLatitude(double targetLatitude) {
        this.targetLatitude = targetLatitude;
    }

    @Override
    public String toString() {
        return "Request{" +
                "globalTime=" + globalTime +
                ", globalCost=" + globalCost +
                ", resultNumbers=" + resultNumbers +
                ", templateName='" + templateName + '\'' +
                ", serviceSequence=" + Arrays.toString(serviceSequence) +
                ", consumerId='" + consumerId + '\'' +
                ", targetLongitude=" + targetLongitude +
                ", targetLatitude=" + targetLatitude +
                '}';
    }
}
