package com.twitter.anubhav.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Getter
public class OverRidenProp extends Prop {
    private String overRide;

    public OverRidenProp(String key, Object value, String overRide) {
        super(key, value);
        this.overRide = overRide;
    }
}
