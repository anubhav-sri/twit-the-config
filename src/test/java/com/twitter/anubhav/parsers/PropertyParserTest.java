package com.twitter.anubhav.parsers;

import com.twitter.anubhav.exceptions.ConfigFormatException;
import com.twitter.anubhav.models.Props;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PropertyParserTest {

    private PropertyParser propertyParser;

    @BeforeEach
    void setUp() {
        propertyParser = new PropertyParser();
    }

    @Test
    void shouldThrowConfigFormatExceptionIfPropertyNotInCorrectFormat() {
        Assertions.assertThrows(ConfigFormatException.class,
                () -> new PropertyParser().parseProp("p-invalid"));
    }

    @Test
    void shouldReturnPropsWithKeyAndValue() {
        Props actualProp = propertyParser.parseProp("p=valid");

        assertThat(actualProp).isEqualTo(new Props("p", "valid"));
    }

    @Test
    void shouldReturnPropsWithOverRidesAndKeyAndValue() {
        Props actualProp = propertyParser.parseProp("p<ubuntu>=valid");

        assertThat(actualProp).isEqualTo(new Props("p<ubuntu>", "valid"));
    }

}