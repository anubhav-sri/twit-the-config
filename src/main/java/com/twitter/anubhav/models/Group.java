package com.twitter.anubhav.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Getter
public class Group {
    private List<Prop> propList = new ArrayList<>();
    private String name;

    public Group(String name) {
        this.name = name;
    }

    public void addProperty(Prop prop) {
        if (prop instanceof OverRiddenProp) {
            propList.stream().filter(p -> p.getKey().equals(prop.getKey())).findFirst().ifPresent(
                    pr -> {
                        OverRiddenProp overriddenProp = (OverRiddenProp) prop;
                        pr.addOverRides(overriddenProp.getOverRide(), overriddenProp.getValue());
                    }
            );
        } else
            propList.add(prop);
    }
}
