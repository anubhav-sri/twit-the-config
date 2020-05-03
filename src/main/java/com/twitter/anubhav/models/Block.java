package com.twitter.anubhav.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Getter
public class Block {
    private List<Props> propsList = new ArrayList<>();
    private String name;

    public Block(String name) {
        this.name = name;
    }

    public void addProperty(Props prop) {
        propsList.add(prop);
    }
}
