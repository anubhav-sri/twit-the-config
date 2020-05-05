package com.twitter.anubhav.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@Getter
public class Prop {
    private final String key;
    private final Object value;
    private Map<String, Object> overRides = new LinkedHashMap<>();

    public Prop(String key, Object value) {
        this.key = key.trim();
        this.value = value;
    }

    public void addOverRides(String overRide, Object value) {
        overRides.put(overRide, value);
    }

    public Object getValue(List<String> activeOverrides) {
        Object finalValue = this.value;
        for (Map.Entry<String, Object> overriddenEntrySet : overRides.entrySet()) {
            if (activeOverrides.contains(overriddenEntrySet.getKey()))
                finalValue = overriddenEntrySet.getValue();
        }
        return finalValue;
    }
}
