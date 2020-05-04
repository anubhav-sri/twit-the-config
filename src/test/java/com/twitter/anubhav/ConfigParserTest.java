package com.twitter.anubhav;

import com.twitter.anubhav.exceptions.ConfigFormatException;
import com.twitter.anubhav.models.Block;
import com.twitter.anubhav.models.Prop;
import com.twitter.anubhav.parsers.PropertyParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfigParserTest {

    private ConfigParser configParser;
    @Mock
    private PropertyParser propertyParser;

    @BeforeEach
    void setUp() {
        configParser = new ConfigParser(propertyParser);
    }

    @Test
    void shouldThrowConfigFormatExceptionWhenFileDoesNotStartsWithBlock() {
        Assertions.assertThrows(ConfigFormatException.class,
                () -> configParser.parseBlocks(Stream.of("p=1", "[block]")));

    }

    @Test
    void shouldReturnBlockWithEmptyProps() {
        List<Block> blocks = configParser.parseBlocks(Stream.of("[block]"));
        assertThat(blocks).hasSize(1);
        assertThat(blocks.get(0)).isEqualTo(new Block("block"));

    }

    @Test
    void shouldReturnBlockWithProps() {
        Block expectedBlock = new Block("block");
        Prop expectedProp = new Prop("p", 1);
        expectedBlock.addProperty(expectedProp);

        when(propertyParser.parseProp("p=1")).thenReturn(expectedProp);

        List<Block> blocks = configParser.parseBlocks(Stream.of("[block]", "p=1"));

        assertThat(blocks).hasSize(1);
        assertThat(blocks.get(0)).isEqualTo(expectedBlock);

    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void shouldReturnMultipleBlocksWithMultipleProps() {
        Block expectedBlock1 = new Block("block");
        Prop expectedProp1 = new Prop("p", 1);
        Prop expectedProp2 = new Prop("p", 1);
        expectedBlock1.addProperty(expectedProp1);
        expectedBlock1.addProperty(expectedProp2);

        Block expectedBlock2 = new Block("block1");
        Prop expectedProp3 = new Prop("p", 2);
        expectedBlock2.addProperty(expectedProp3);

        when(propertyParser.parseProp("p=1")).thenReturn(expectedProp1);
        when(propertyParser.parseProp("p=2")).thenReturn(expectedProp3);
        when(propertyParser.parseProp("p=4")).thenReturn(expectedProp2);

        List<Block> blocks = configParser.parseBlocks(Stream.of("[block]", "p=1", "p=4", "[block1]", "p=2"));

        assertThat(blocks).containsAll(List.of(expectedBlock1, expectedBlock2));

    }

}