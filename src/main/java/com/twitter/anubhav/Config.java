package com.twitter.anubhav;

import java.util.HashMap;

public class Config {

    private HashMap<String, Object> blocks = new HashMap<>();

    public String get(String key) {
        return blocks.get(key).toString();
    }

    public void add(String config1) {
        blocks.put(config1, "");
    }
}
