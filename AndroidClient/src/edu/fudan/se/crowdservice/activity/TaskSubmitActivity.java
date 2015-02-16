package edu.fudan.se.crowdservice.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.core.dui.KeyValueView;
import edu.fudan.se.crowdservice.jade.AgentManager;
import edu.fudan.se.crowdservice.jade.JADEService;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.wrapper.DelegateWrapper;
import edu.fudan.se.crowdservice.wrapper.ResponseWrapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static edu.fudan.se.crowdservice.R.id.parameter_view;

/**
 * Created by Jiahuan on 2015/1/26.
 */
public class TaskSubmitActivity extends Activity {

    public static final String EXTRA_BUNDLE = "ExtraBundle";
    public static final String UI_MODEL = "ui-model";

    private AgentManager am;
    private long taskId;
    private List<KeyValueView> keyValueViews;

    private ServiceConnection jadeConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            am = (AgentManager) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_submit);
        LinearLayout parameterView = (LinearLayout) findViewById(parameter_view);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        keyValueViews = new LinkedList<KeyValueView>();

        DelegateWrapper wrapper = (DelegateWrapper) getIntent().getBundleExtra(EXTRA_BUNDLE).getSerializable(UI_MODEL);
        taskId = wrapper.taskId;
        for (KeyValueHolder holder : wrapper.keyValueHolders) {
            String viewClassName = "edu.fudan.se.crowdservice.core.dui." + holder.getClass().getSimpleName() + "View";
            try {
                Class clazz = Class.forName(viewClassName);
                KeyValueView view = (KeyValueView) clazz.getConstructor(Context.class, KeyValueHolder.class).newInstance(this, holder);
                parameterView.addView(view, params);
                keyValueViews.add(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        bindService(new Intent(getApplicationContext(), JADEService.class), jadeConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(jadeConnection);
        super.onDestroy();
    }

    public void submit(View v) {
        ArrayList<KeyValueHolder> result = new ArrayList<KeyValueHolder>();
        for (KeyValueView view : keyValueViews) {
            if (view.needSubmit()) {
                if (view.isReady()) {
                    result.add(view.submit());
                } else {
                    Toast.makeText(this, R.string.please_complete_task_form, Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        am.sendResponse(new ResponseWrapper(taskId, result));
        finish();
    }
}