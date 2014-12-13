package edu.fudan.se.crowdservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import edu.fudan.se.crowdservice.jade.JADEService;
import edu.fudan.se.crowdservice.jade.JADEServiceIntent;
import edu.fudan.se.crowdservice.jade.TemplateExecutingAgent;

public class MainActivity extends Activity {
    private TemplateExecutingAgent agent;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            agent = ((JADEService.AgentBinder) iBinder).getAgent();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            agent = null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bindService(new JADEServiceIntent(this), connection, BIND_AUTO_CREATE);
    }
}
