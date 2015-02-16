package edu.fudan.se.crowdservice.core.dui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import edu.fudan.se.crowdservice.kv.ImageDisplay;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;

import java.io.FileNotFoundException;

/**
 * Created by Jiahuan on 2015/1/26.
 */
public class ImageDisplayView extends KeyValueView<byte[]> {

    public ImageDisplayView(Context context, KeyValueHolder<byte[]> holder) {
        super(context, holder);
        setOrientation(VERTICAL);

        TextView description = new TextView(getContext());
        description.setText(holder.getKey());
        ImageView image = new ImageView(getContext());
        try {
            String imagePath = ((ImageDisplay) holder).imagePath;
            Log.i("imagePath", imagePath);
            image.setImageBitmap(BitmapFactory.decodeStream(context.openFileInput(imagePath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
