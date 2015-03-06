package edu.fudan.se.crowdservice.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.activity.MainActivity;
import edu.fudan.se.crowdservice.core.dui.KeyValueView;
import edu.fudan.se.crowdservice.jade.agent.AgentInterface;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.view.CountDownButton;
import edu.fudan.se.crowdservice.wrapper.DelegateWrapper;
import edu.fudan.se.crowdservice.wrapper.RefuseWrapper;
import edu.fudan.se.crowdservice.wrapper.ResponseWrapper;

import java.util.ArrayList;

/**
 * Created by Dawnwords on 2015/3/2.
 */
public class TaskSubmitFragment extends Fragment {
    private DelegateWrapper delegateWrapper;
    private ArrayList<KeyValueView> keyValueViews;
    private CountDownButton submit;
    private AgentInterface agent;
    private LinearLayout parameter;

    public TaskSubmitFragment() {
        keyValueViews = new ArrayList<KeyValueView>();
    }

    public void setAgent(AgentInterface agent) {
        this.agent = agent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isVisible()) {
            inflater.inflate(R.menu.consumer_session, menu);
            ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_back) {
            switchWorkerFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchWorkerFragment() {
        ((MainActivity) getActivity()).onNavigationDrawerItemSelected("Worker");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_submit, container, false);
        submit = (CountDownButton) v.findViewById(R.id.task_submit);
        submit.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        }).setTimeUpListener(new CountDownButton.TimeUpListener() {
            @Override
            public void onTimeUp() {
                submit.setClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchWorkerFragment();
                    }
                }).setText(R.string.time_up);
                RefuseWrapper refuseWrapper = new RefuseWrapper(delegateWrapper.taskId, RefuseWrapper.Reason.OFFER_OUT_OF_DATE);
                ((WorkerFragment) getActivity().getSupportFragmentManager().findFragmentByTag("Worker")).addMessageWrapper(refuseWrapper);
            }
        });
        parameter = (LinearLayout) v.findViewById(R.id.task_parameter);
        return v;
    }

    public void setDelegateWrapper(DelegateWrapper delegateWrapper, long ddl) {
        this.delegateWrapper = delegateWrapper;
        this.keyValueViews.clear();
        this.parameter.removeAllViews();
        addKeyValueViews(delegateWrapper.keyValueHolders);
        submit.setTimeRemain(ddl).start();
    }

    private void addKeyValueViews(ArrayList<KeyValueHolder> keyValueHolders) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        for (KeyValueHolder holder : keyValueHolders) {
            String viewClassName = "edu.fudan.se.crowdservice.core.dui." + holder.getClass().getSimpleName() + "View";
            try {
                Class clazz = Class.forName(viewClassName);
                KeyValueView view = (KeyValueView) clazz.getConstructor(Context.class, KeyValueHolder.class).newInstance(getActivity(), holder);
                parameter.addView(view, params);
                keyValueViews.add(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void submit() {
        ArrayList<KeyValueHolder> result = new ArrayList<KeyValueHolder>();
        for (KeyValueView view : keyValueViews) {
            if (view.needSubmit()) {
                if (view.isReady()) {
                    result.add(view.submit());
                } else {
                    Toast.makeText(getActivity(), R.string.please_complete_task_form, Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        submit.stop();
        agent.sendResponse(new ResponseWrapper(delegateWrapper.taskId, result));
        switchWorkerFragment();
    }
}
