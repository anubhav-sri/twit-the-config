package com.twitter.anubhav.models;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PropTest {

    @Test
    void shouldReturnTheOverRiddenValueIfAvailable() {
        Prop prop = new Prop("key", "value");
        prop.addOverRides("override1", "value1");

        assertThat(prop.getValue(List.of("override1"))).isEqualTo("value1");
    }

    @Test
    void shouldReturnTheDefaultValueIfOverRideNotAvailable() {
        Prop prop = new Prop("key", "value");

        assertThat(prop.getValue(List.of("override1"))).isEqualTo("value");
    }

    @Test
    void shouldReturnTheDefaultLastOverRiddenValueWhenBothArePresent() {
        Prop prop = new Prop("key", "value");
        prop.addOverRides("override1", "value1");
        prop.addOverRides("override2", "value2");

        assertThat(prop.getValue(List.of("override2", "override1"))).isEqualTo("value2");
    }

    @Test
    void shouldTrimTheBlankSpacesFromKey() {
        Prop prop = new Prop("key ", "value ");
        prop.addOverRides("override1", "value1");

        assertThat(prop.getValue(List.of("override1"))).isEqualTo("value1");
        assertThat(prop.getValue(Lists.emptyList())).isEqualTo("value ");
        assertThat(prop.getKey()).isEqualTo("key");
    }

}