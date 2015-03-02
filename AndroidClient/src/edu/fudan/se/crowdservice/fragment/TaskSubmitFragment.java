package edu.fudan.se.crowdservice.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.core.dui.KeyValueView;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.wrapper.DelegateWrapper;
import edu.fudan.se.crowdservice.wrapper.ResponseWrapper;

import java.util.ArrayList;

/**
 * Created by Dawnwords on 2015/3/2.
 */
public class TaskSubmitFragment extends BaseFragment<KeyValueHolder> {
    private DelegateWrapper delegateWrapper;
    private ArrayList<KeyValueView> keyValueViews;

    public TaskSubmitFragment() {
        super(R.string.no_task, R.layout.list_item_key_value);
        keyValueViews = new ArrayList<KeyValueView>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getListView().addFooterView(initSubmitButton());
        super.onViewCreated(view, savedInstanceState);
    }

    private Button initSubmitButton() {
        Button submit = new Button(getActivity());
        submit.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        submit.setText(getText(R.string.submit));
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        return submit;
    }

    @Override
    protected void setItemView(KeyValueHolder holder, View convertView) {
        String viewClassName = "edu.fudan.se.crowdservice.core.dui." + holder.getClass().getSimpleName() + "View";
        try {
            Class clazz = Class.forName(viewClassName);
            KeyValueView view = (KeyValueView) clazz.getConstructor(Context.class, KeyValueHolder.class).newInstance(this, holder);
            ((LinearLayout) convertView).addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            keyValueViews.add(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDelegateWrapper(DelegateWrapper delegateWrapper) {
        this.delegateWrapper = delegateWrapper;
        setData(delegateWrapper.keyValueHolders);
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
        agent.sendResponse(new ResponseWrapper(delegateWrapper.taskId, result));
    }
}
