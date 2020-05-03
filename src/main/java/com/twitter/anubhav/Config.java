package com.twitter.anubhav;

import com.twitter.anubhav.models.Block;
import com.twitter.anubhav.models.Props;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class Config {

    private HashMap<String, Map<String, Object>> blocks = new HashMap<>();

    public Map<String, Object> get(String key) {
        return blocks.getOrDefault(key, new HashMap<>());
    }

    public void addBlock(Block block) {
        Map<String, Object> propMap = block.getPropsList()
                .stream()
                .collect(toMap(Props::getKey, Props::getValue));
        blocks.put(block.getName(), propMap);
    }
}
