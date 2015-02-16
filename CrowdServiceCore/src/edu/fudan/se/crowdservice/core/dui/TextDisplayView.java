package edu.fudan.se.crowdservice.core.dui;

import android.content.Context;
import android.widget.TextView;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;

/**
 * Created by Jiahuan on 2015/1/26.
 */
public class TextDisplayView extends KeyValueView<String> {
    public TextDisplayView(Context context, KeyValueHolder<String> holder) {
        super(context, holder);
        setOrientation(HORIZONTAL);
        TextView keyView = new TextView(getContext());
        TextView valueView = new TextView(getContext());

        keyView.setText(holder.getKey());
        valueView.setText(holder.getValue());

        addView(keyView, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3));
        addView(valueView, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 7));
    }


    @Override
    public boolean needSubmit() {
        return false;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public KeyValueHolder<String> submit() {
        return null;
    }
}
