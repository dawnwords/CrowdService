package edu.fudan.se.crowdservice.kv;

import edu.fudan.se.crowdservice.util.IOUtil;

/**
 * Created by Jiahuan on 2015/1/25.
 */
public class Image extends KeyValueHolder<byte[]> {


    private String imagePath;

    public Image(String key, String imagePath) {
        super(key, null);
        this.imagePath = imagePath;
    }

    @Override
    public byte[] getValue() {
        return IOUtil.loadByteArray(imagePath);
    }
}
