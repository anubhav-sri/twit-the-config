package com.twitter.anubhav.dto;

import com.twitter.anubhav.models.Group;
import com.twitter.anubhav.models.Prop;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class Config {

    private HashMap<String, Map<String, Object>> blocks = new HashMap<>();

    public Map<String, Object> get(String key) {
        return blocks.getOrDefault(key, new HashMap<>());
    }

    public void addBlock(Group group) {
        Map<String, Object> propMap = createMapForProps(group);
        blocks.put(group.getName(), propMap);
    }

    private Map<String, Object> createMapForProps(Group group) {
        return group.getPropList()
                .stream()
                .collect(toMap(Prop::getKey, Prop::getValue));
    }
}
