package edu.fudan.se.crowdservice.kv;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class Description extends KeyValueHolder<String> {
    private static final long serialVersionUID = 5252735990668788681L;

    public Description(String value) {
        super("", value);
    }
}
