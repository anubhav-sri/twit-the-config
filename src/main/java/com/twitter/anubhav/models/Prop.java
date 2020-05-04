package com.twitter.anubhav.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class Prop {
    private final String key;
    private final Object value;

    public Prop(String key, Object value) {
        this.key = key;
        this.value = value;
    }
}
