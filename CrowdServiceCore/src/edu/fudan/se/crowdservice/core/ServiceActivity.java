package edu.fudan.se.crowdservice.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;

/**
 * Created by Dawnwords on 2014/4/21.
 */
public abstract class ServiceActivity {

    public static final String EXTRA_BUNDLE = "ExtraBundle";

    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void onCreate(Bundle savedInstanceState) {
    }

    public void onStart() {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onStop() {
    }

    public void onRestart() {
    }

    public void onDestroy() {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }

    public void onTouchEvent(MotionEvent event) {
    }

    public void onKeyDown(int keyCode, KeyEvent event) {
    }

    public void runOnUiThread(Runnable r) {
        activity.runOnUiThread(r);
    }

    protected void finish() {
        finish(null);
    }

    protected void finish(Object value) {
        activity.finish();
        ResultHolder holder = ActivityResult.getInstance().getServiceActivityResultHolder();
        if (holder == null) {
            throw new IllegalAccessError("ResultHolder is Null!");
        }
        holder.set(value);
    }

    protected void addContentView(View view, ViewGroup.LayoutParams params) {
        activity.addContentView(view, params);
    }

    protected void setContentView(View view) {
        activity.setContentView(view);
    }

    protected void setContentView(View view, ViewGroup.LayoutParams params) {
        activity.setContentView(view, params);
    }

    protected Context getContext() {
        return activity;
    }

    protected Context getApplicationContext() {
        return activity.getApplicationContext();
    }

    protected LayoutInflater getLayoutInflater() {
        return activity.getLayoutInflater();
    }

    protected Resources getResources() {
        return activity.getResources();
    }

    protected Object getSystemService(String name) {
        return activity.getSystemService(name);
    }

    protected Object getParameter(String name) {
        return activity.getIntent().getBundleExtra(EXTRA_BUNDLE).get(name);
    }

    protected PackageManager getPackageManager() {
        return activity.getPackageManager();
    }

    protected Bundle getInputParameterBundle() {
        return activity.getIntent().getBundleExtra(EXTRA_BUNDLE);
    }

    protected WindowManager getWindowManager(){
        return activity.getWindowManager();
    }

    protected Window getWindow(){
        return activity.getWindow();
    }

    protected void toast(String format,Object... args){
        Toast.makeText(getContext(),String.format(format,args),Toast.LENGTH_LONG).show();
    }

    protected int dp2px(float dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
