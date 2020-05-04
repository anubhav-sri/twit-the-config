package com.twitter.anubhav.dto;

import com.twitter.anubhav.models.Group;
import com.twitter.anubhav.models.Prop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toUnmodifiableMap;

public class Config {

    private Map<String, Map<String, Object>> blocks = new HashMap<>();

    public Map<String, Object> get(String key) {
        return blocks.getOrDefault(key, new HashMap<>());
    }

    public void addBlock(Group group, List<String> overRides) {
        Map<String, Object> propMap = createMapForProps(group, overRides);
        blocks.put(group.getName(), propMap);
    }

    private Map<String, Object> createMapForProps(Group group, List<String> overRides) {
        return group.getPropList()
                .stream()
                .collect(toUnmodifiableMap(Prop::getKey, prop -> prop.getValue(overRides)));
    }
}
