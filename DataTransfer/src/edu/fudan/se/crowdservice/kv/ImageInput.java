package edu.fudan.se.crowdservice.kv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class ImageInput extends KeyValueHolder<byte[]> {
    public ImageInput(String key, byte[] value) {
        super(key, value);
    }

    public ImageInput(String key, String imagePath) {
        this(key, getByteArray(imagePath));
    }

    private static byte[] getByteArray(String imagePath) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 10, bitmap.getHeight() / 10, true).compress(Bitmap.CompressFormat.JPEG, 50, out);
        return Base64.encode(out.toByteArray(), Base64.DEFAULT);
    }
}
