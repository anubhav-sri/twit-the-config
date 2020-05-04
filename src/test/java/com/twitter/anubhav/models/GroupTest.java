package com.twitter.anubhav.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GroupTest {

    @Test
    void shouldAddOverrideToTheExistingProperty() {
        Group group = new Group("aGroup");
        group.addProperty(new Prop("key", "value"));
        group.addProperty(new OverRiddenProp("key", "value-override", "ubuntu"));

        Prop expected = new Prop("key", "value");
        expected.addOverRides("ubuntu", "value-override");
        assertThat(group.getPropList().get(0)).isEqualTo(expected);
    }
}