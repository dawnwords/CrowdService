package edu.fudan.se.crowdservice.kv;

import android.annotation.TargetApi;
import android.os.Build;

/**
 * Created by Jiahuan on 2015/1/27.
 */
@TargetApi(Build.VERSION_CODES.FROYO)
public class Image extends KeyValueHolder<byte[]> {
    private static final long serialVersionUID = 6548805395073533778L;

    public final String imagePath;

    public Image(String key, String imagePath) {
        super(key, null);
        this.imagePath = imagePath;
    }

}
