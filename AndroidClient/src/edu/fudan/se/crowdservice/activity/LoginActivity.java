package edu.fudan.se.crowdservice.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.core.SavedProperty;
import edu.fudan.se.crowdservice.felix.FelixService;
import edu.fudan.se.crowdservice.jade.AgentManager;
import edu.fudan.se.crowdservice.jade.JADEService;
import edu.fudan.se.crowdservice.view.LoadingDialog;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class LoginActivity extends Activity {
    private EditText agentNameEditText, capacityEditText, jadeIPEditText,
            jadePortEditText, obrIPEditText, obrPortEditText;
    private SharedPreferences settings;
    private ProgressDialog dialog;
    private AgentManager am;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            am = (AgentManager) iBinder;
            am.registerOnAgentReadyInterface(new AgentManager.OnAgentReadyInterface() {
                @Override
                public void onAgentReady() {
                    startService(new Intent(getApplicationContext(), FelixService.class));
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    finish();
                }

                @Override
                public void onAgentFail() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, getString(R.string.jade_fail), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            am = null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settings = getSharedPreferences(SavedProperty.CROWD_SERVICE, 0);
        initEditText();
    }

    @Override
    protected void onDestroy() {
        if (am != null) {
            unbindService(connection);
        }
        super.onDestroy();
    }

    private void initEditText() {
        agentNameEditText = (EditText) findViewById(R.id.agent_name);
        capacityEditText = (EditText) findViewById(R.id.capacity);
        jadeIPEditText = (EditText) findViewById(R.id.jade_ip);
        jadePortEditText = (EditText) findViewById(R.id.jade_port);
        obrIPEditText = (EditText) findViewById(R.id.obr_ip);
        obrPortEditText = (EditText) findViewById(R.id.obr_port);

        agentNameEditText.setText(settings.getString(SavedProperty.AGENT_NAME, ""));
        capacityEditText.setText(settings.getString(SavedProperty.CAPACITY, ""));
        jadeIPEditText.setText(settings.getString(SavedProperty.JADE_IP, ""));
        jadePortEditText.setText(String.valueOf(settings.getInt(SavedProperty.JADE_PORT, 1099)));
        obrIPEditText.setText(settings.getString(SavedProperty.OBR_IP, ""));
        obrPortEditText.setText(String.valueOf(settings.getInt(SavedProperty.OBR_PORT, 80)));
    }

    public void doLogin(View v) {
        dialog = LoadingDialog.show(this, getString(R.string.loggingin), new Handler(), new Runnable() {
            @Override
            public void run() {
                String agentName = agentNameEditText.getText().toString();
                String capacity = capacityEditText.getText().toString();
                String jadeIp = jadeIPEditText.getText().toString();
                String obrIp = obrIPEditText.getText().toString();
                int jadePort = Integer.parseInt(jadePortEditText.getText().toString());
                int obrPort = Integer.parseInt(obrPortEditText.getText().toString());

                //TODO check input
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(SavedProperty.AGENT_NAME, agentName);
                editor.putString(SavedProperty.CAPACITY, capacity);
                editor.putString(SavedProperty.JADE_IP, jadeIp);
                editor.putString(SavedProperty.OBR_IP, obrIp);
                editor.putInt(SavedProperty.JADE_PORT, jadePort);
                editor.putInt(SavedProperty.OBR_PORT, obrPort);
                editor.commit();

                bindService(new Intent(getApplicationContext(), JADEService.class), connection, BIND_AUTO_CREATE);
            }
        });
    }

}