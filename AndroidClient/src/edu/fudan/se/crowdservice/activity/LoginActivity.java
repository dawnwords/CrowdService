package edu.fudan.se.crowdservice.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import edu.fudan.se.crowdservice.CrowdServiceApplication;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.felix.FelixService;
import edu.fudan.se.crowdservice.jade.JADEService;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class LoginActivity extends Activity {
    private EditText agentNameEditText, capacityEditText;
    private SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        settings = getSharedPreferences(CrowdServiceApplication.CROWD_SERVICE, 0);
        initEditText();
    }

    private void initEditText() {
        agentNameEditText = (EditText) findViewById(R.id.agent_name);
        capacityEditText = (EditText) findViewById(R.id.capacity);

        agentNameEditText.setText(settings.getString(CrowdServiceApplication.CAPACITY, ""));
        capacityEditText.setText(settings.getString(CrowdServiceApplication.AGENT_NAME, ""));
    }

    public void doLogin(View v) {
        String agentName = agentNameEditText.getText().toString();
        String capacity = capacityEditText.getText().toString();

        //TODO check input
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(CrowdServiceApplication.AGENT_NAME, agentName);
        editor.putString(CrowdServiceApplication.CAPACITY, capacity);
        editor.commit();

        startService(new Intent(getApplicationContext(), JADEService.class));
        startService(new Intent(getApplicationContext(), FelixService.class));
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}