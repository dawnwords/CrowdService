package edu.fudan.se.crowdservice.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.activity.MainActivity;
import edu.fudan.se.crowdservice.core.dui.KeyValueView;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.view.CountDownButton;
import edu.fudan.se.crowdservice.wrapper.DelegateWrapper;
import edu.fudan.se.crowdservice.wrapper.ResponseWrapper;

import java.util.ArrayList;

/**
 * Created by Dawnwords on 2015/3/2.
 */
public class TaskSubmitFragment extends ChildFragment<KeyValueHolder> {
    private DelegateWrapper delegateWrapper;
    private ArrayList<KeyValueView> keyValueViews;
    private CountDownButton submit;

    public TaskSubmitFragment() {
        super(R.string.no_task, R.layout.list_item_key_value, "Worker");
        keyValueViews = new ArrayList<KeyValueView>();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initSubmitButton();
        getListView().addFooterView(submit);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initSubmitButton() {
        submit = new CountDownButton(getActivity(), getText(R.string.submit).toString())
                .setClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        submit();
                    }
                }).setTimeUpListener(new CountDownButton.TimeUpListener() {
                    @Override
                    public void onTimeUp() {
                        submit.setText(R.string.time_up);
                        submit.setClickable(false);
                    }
                });
        submit.setClickable(true);
    }

    @Override
    protected void setItemView(KeyValueHolder holder, View convertView) {
        String viewClassName = "edu.fudan.se.crowdservice.core.dui." + holder.getClass().getSimpleName() + "View";
        try {
            Class clazz = Class.forName(viewClassName);
            KeyValueView view = (KeyValueView) clazz.getConstructor(Context.class, KeyValueHolder.class).newInstance(getActivity(), holder);
            LinearLayout group = (LinearLayout) convertView;
            group.removeAllViews();
            group.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            keyValueViews.add(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDelegateWrapper(DelegateWrapper delegateWrapper, long ddl) {
        this.delegateWrapper = delegateWrapper;
        this.keyValueViews.clear();
        setData(delegateWrapper.keyValueHolders);
        submit.setTimeRemain(ddl).start();
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
        ((MainActivity) getActivity()).onNavigationDrawerItemSelected("Worker");
    }
}
