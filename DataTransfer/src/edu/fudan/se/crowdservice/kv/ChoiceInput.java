package edu.fudan.se.crowdservice.kv;

/**
 * Created by Jiahuan on 2015/1/25.
 */
public class ChoiceInput extends KeyValueHolder<String[]> {
    private static final long serialVersionUID = 8112066867190180761L;

    public ChoiceInput(String key, String options) {
        super(key, options.split(","));
    }
}
