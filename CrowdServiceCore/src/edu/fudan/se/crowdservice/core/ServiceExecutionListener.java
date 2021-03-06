package edu.fudan.se.crowdservice.core;

/**
 * Created by Dawnwords on 2015/3/6.
 */
public interface ServiceExecutionListener {
    boolean onServiceStart(ConcreteService serviceClass, Object[] args);

    void onServiceStop(ConcreteService serviceClass);

    void onServiceException(ConcreteService serviceClass, String reason);

    void onTemplateStop();

    String onRequestUserInput(String msg);

    boolean onRequestUserConfirm(String msg);

    int onRequestUserChoose(String msg, String[] items);

    void onShowMessage(String msg);
}