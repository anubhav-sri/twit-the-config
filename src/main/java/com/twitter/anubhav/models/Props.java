package com.twitter.anubhav.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class Props {
    private final String key;
    private final Object value;

    public Props(String key, Object value) {
        this.key = key;
        this.value = value;
    }
}
