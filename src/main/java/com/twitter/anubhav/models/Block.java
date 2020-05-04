package com.twitter.anubhav.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Getter
public class Block {
    private List<Prop> propList = new ArrayList<>();
    private String name;

    public Block(String name) {
        this.name = name;
    }

    public void addProperty(Prop prop) {
        propList.add(prop);
    }
}
