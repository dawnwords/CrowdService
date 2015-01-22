package edu.fudan.se.crowdservice.bean.kv;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class ImageDisplay extends KeyValueHolder<byte[]> {
    public ImageDisplay(String key, byte[] value) {
        super(key, value);
    }
}
