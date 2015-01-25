package edu.fudan.se.crowdservice.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.felix.FelixService;
import edu.fudan.se.crowdservice.felix.TemplateManager;
import edu.fudan.se.crowdservice.fragment.BaseFragment;
import edu.fudan.se.crowdservice.fragment.ConsumerFragment;
import edu.fudan.se.crowdservice.fragment.WorkerFragment;
import edu.fudan.se.crowdservice.jade.AgentManager;
import edu.fudan.se.crowdservice.jade.JADEService;

public class MainActivity extends Activity {
    private ConsumerFragment consumerFragment;
    private ServiceConnection felixConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TemplateManager tm = (TemplateManager) iBinder;
            consumerFragment.setTemplateManager(tm);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            consumerFragment.setTemplateManager(null);
        }
    };
    private WorkerFragment workerFragment;
    private ServiceConnection jadeConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AgentManager am = (AgentManager) iBinder;
            workerFragment.setAgent(am);
            consumerFragment.setAgent(am);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            workerFragment.setAgent(null);
            consumerFragment.setAgent(null);
        }
    };
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        consumerFragment = new ConsumerFragment();
        workerFragment = new WorkerFragment();

        fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_content, consumerFragment);
        transaction.add(R.id.main_content, workerFragment);
        transaction.hide(workerFragment);
        transaction.commit();

        bindService(new Intent(getApplicationContext(), JADEService.class), jadeConnection, BIND_AUTO_CREATE);
        bindService(new Intent(getApplicationContext(), FelixService.class), felixConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(felixConnection);
        unbindService(jadeConnection);
        super.onDestroy();
    }

    public void workerFragment(View v) {
        replaceFragment(workerFragment, consumerFragment);
    }

    public void consumerFragment(View v) {
        replaceFragment(consumerFragment, workerFragment);
        consumerFragment.loadAvailableTemplates();
    }

    private void replaceFragment(BaseFragment newFragment, BaseFragment oldFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(oldFragment);
        transaction.show(newFragment);
        transaction.commit();
    }
}
