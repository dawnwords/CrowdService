package edu.fudan.se.crowdservice.core;

/**
 * Created by Dawnwords on 2015/3/6.
 */
public interface ServiceExecutionListener {
    void onServiceStart(Class serviceClass);

    void onServiceStop(Class serviceClass);

    void onServiceException(Class serviceClass, String reason);

    void onTemplateStop();

    String onRequestUserInput(String msg);

    boolean onRequestUserConfirm(String msg);

    int onRequestUserChoose(String msg, String[] items);

    void onShowMessage(String msg);
}