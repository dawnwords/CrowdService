package edu.fudan.se.crowdservice.core.dui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import edu.fudan.se.crowdservice.kv.ChoiceInput;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;

/**
 * Created by Jiahuan on 2015/1/26.
 */
public class ChoiceInputView extends KeyValueView<String[]> {

    private Spinner spinner;
    private TextView description;

    public ChoiceInputView(Context context, final KeyValueHolder<String[]> holder) {
        super(context, holder);
        setOrientation(HORIZONTAL);
        description = new TextView(getContext());
        description.setText(holder.getKey());

        spinner = new Spinner(getContext());
        spinner.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return holder.getValue().length;
            }

            @Override
            public String getItem(int i) {
                return holder.getValue()[i];
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = new TextView(getContext());
                }
                ((TextView) view).setText(getItem(i));
                return view;
            }
        });
        addView(description, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3));
        addView(spinner, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 7));
    }


    @Override
    public boolean needSubmit() {
        return true;
    }

    @Override
    public boolean isReady() {
        return spinner.getSelectedItem() != null && !"".equals(spinner.getSelectedItem().toString());
    }

    @Override
    public KeyValueHolder<String[]> submit() {
        return new ChoiceInput(description.getText().toString(), spinner.getSelectedItem().toString());
    }
}
