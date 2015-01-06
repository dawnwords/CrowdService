package edu.fudan.se.crowdservice.core;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * Created by Dawnwords on 2014/4/20.
 */
public class AdapterActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceActivityHolder.activity.setActivity(this);
        ServiceActivityHolder.activity.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ServiceActivityHolder.activity.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ServiceActivityHolder.activity.onResume();
    }

    @Override
    protected void onPause() {
        ServiceActivityHolder.activity.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        ServiceActivityHolder.activity.onStop();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ServiceActivityHolder.activity.onRestart();
    }

    @Override
    protected void onDestroy() {
        ServiceActivityHolder.activity.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ServiceActivityHolder.activity.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ServiceActivityHolder.activity.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ServiceActivityHolder.activity.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    static class ServiceActivityHolder {
        private static ServiceActivity activity;

        public static void setActivity(ServiceActivity activity) {
            ServiceActivityHolder.activity = activity;
        }
    }
}