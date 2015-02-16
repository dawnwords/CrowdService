package edu.fudan.se.crowdservice.core.dui;

import android.content.Context;
import android.widget.LinearLayout;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;

/**
 * Created by Jiahuan on 2015/1/26.
 */
public abstract class KeyValueView<T> extends LinearLayout {

    protected KeyValueHolder<T> holder;

    public KeyValueView(Context context, KeyValueHolder<T> holder) {
        super(context);
        this.holder = holder;
    }

    public abstract boolean needSubmit();

    public abstract boolean isReady();

    public abstract KeyValueHolder<T> submit();

}
