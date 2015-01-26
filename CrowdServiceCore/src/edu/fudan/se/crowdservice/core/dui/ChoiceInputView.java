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

    public ChoiceInputView(Context context, KeyValueHolder<String[]> holder) {
        super(context, holder);
    }

    @Override
    protected void render(String key, final String[] value) {
        setOrientation(HORIZONTAL);
        description = new TextView(getContext());
        description.setText(key);

        spinner = new Spinner(getContext());
        spinner.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return value.length;
            }

            @Override
            public String getItem(int i) {
                return value[i];
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view != null) {
                    view = new TextView(getContext());
                }
                ((TextView) view).setText(getItem(i));
                return view;
            }
        });
    }

    @Override
    public boolean needSubmit() {
        return true;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public KeyValueHolder<String[]> submit() {
        return new ChoiceInput(description.getText().toString(), spinner.getSelectedItem().toString());
    }
}
