package com.twitter.anubhav.models;

import org.junit.jupiter.api.Test;

class GroupTest {

    @Test
    public void shouldAddOverrideToTheExistingProperty() {
        Group group = new Group("aGroup");
        group.addProperty(new Prop("key", "value"));
        group.addProperty(new OverRidenProp("key", "value-override", "ubuntu"));
    }
}