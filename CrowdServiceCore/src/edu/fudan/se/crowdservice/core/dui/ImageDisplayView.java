package edu.fudan.se.crowdservice.core.dui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;

/**
 * Created by Jiahuan on 2015/1/26.
 */
public class ImageDisplayView extends KeyValueView<byte[]> {

    private TextView description;

    public ImageDisplayView(Context context, KeyValueHolder<byte[]> holder) {
        super(context, holder);
    }

    @Override
    protected void render(String key, byte[] value) {
        setOrientation(VERTICAL);

        description = new TextView(getContext());
        description.setText(key);
        ImageView image = new ImageView(getContext());
        image.setImageBitmap(BitmapFactory.decodeByteArray(value, 0, value.length));

        addView(description, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(image, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    public boolean needSubmit() {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public KeyValueHolder<byte[]> submit() {
        return null;
    }
}
