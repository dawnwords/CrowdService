package edu.fudan.se.crowdservice.core;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import edu.fudan.se.crowdservice.core.ServiceActivity;

/**
 * Created by Dawnwords on 2014/4/20.
 */
public class AdapterActivity extends Activity {

    private ServiceActivity activity;

    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String className = getIntent().getStringExtra(ServiceActivity.SERVICE_ACTIVITY_CLASS);
        try {
            Class<ServiceActivity> clazz = (Class<ServiceActivity>) Class.forName(className);
            activity = clazz.newInstance();
            activity.setActivity(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        activity.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        activity.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity.onResume();
    }

    @Override
    protected void onPause() {
        activity.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        activity.onStop();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        activity.onRestart();
    }

    @Override
    protected void onDestroy() {
        activity.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activity.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        activity.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        activity.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}