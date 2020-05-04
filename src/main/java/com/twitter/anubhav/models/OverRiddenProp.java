package com.twitter.anubhav.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Getter
public class OverRiddenProp extends Prop {
    private String overRide;

    public OverRiddenProp(String key, Object value, String overRide) {
        super(key, value);
        this.overRide = overRide;
    }
}
