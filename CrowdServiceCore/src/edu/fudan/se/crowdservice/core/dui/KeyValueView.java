package edu.fudan.se.crowdservice.core.dui;

import android.content.Context;
import android.widget.LinearLayout;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;

/**
 * Created by Jiahuan on 2015/1/26.
 */
public abstract class KeyValueView<T> extends LinearLayout {

    public KeyValueView(Context context, KeyValueHolder<T> holder) {
        super(context);
        render(holder.getKey(), holder.getValue());
    }

    protected abstract void render(String key, T value);

    public abstract boolean needSubmit();

    public abstract boolean isReady();

    public abstract KeyValueHolder<T> submit();

}
