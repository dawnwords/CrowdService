package edu.fudan.se.crowdservice.kv;

import java.io.Serializable;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class KeyValueHolder<T> implements Serializable {

    private static final long serialVersionUID = 1984215843007904910L;

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

    public void setValue(T value) {
        this.value = value;
    }
}
