package edu.fudan.se.crowdservice.bean.kv;

import java.io.Serializable;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class KeyValueHolder<T> implements Serializable {
    private String key;
    private T value;

    public KeyValueHolder(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }
}
