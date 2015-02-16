package edu.fudan.se.crowdservice.core.dui;

import android.content.Context;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.kv.TextInput;

/**
 * Created by Jiahuan on 2015/1/26.
 */
public class TextInputView extends KeyValueView<String> {

    private EditText valueText;
    private TextView keyText;

    public TextInputView(Context context, KeyValueHolder<String> holder) {
        super(context, holder);
        setOrientation(HORIZONTAL);
        keyText = new TextView(getContext());
        valueText = new EditText(getContext());

        keyText.setText(holder.getKey());
        valueText.setText(holder.getValue());

        addView(keyText, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3));
        addView(valueText, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 7));
    }

    @Override
    public boolean needSubmit() {
        return true;
    }

    @Override
    public boolean isReady() {
        Editable editable = valueText.getText();
        return editable != null && !"".equals(editable.toString());
    }

    @Override
    public KeyValueHolder<String> submit() {
        String key = keyText.getText().toString();
        String value = valueText.getText().toString();
        return new TextInput(key, value);
    }
}
