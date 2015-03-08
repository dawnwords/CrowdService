package edu.fudan.se.crowdservice.plan;

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
}
