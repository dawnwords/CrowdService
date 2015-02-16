package edu.fudan.se.crowdservice.core.dui;

import android.content.Context;
import android.widget.TextView;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;

/**
 * Created by Jiahuan on 2015/1/26.
 */
public class DescriptionView extends KeyValueView<String> {

    private TextView descriptionText;

    public DescriptionView(Context context, KeyValueHolder<String> holder) {
        super(context, holder);
        descriptionText = new TextView(getContext());
        descriptionText.setText(holder.getValue());
        addView(descriptionText, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
