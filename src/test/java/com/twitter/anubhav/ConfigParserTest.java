package com.twitter.anubhav;

import com.twitter.anubhav.exceptions.ConfigFormatException;
import com.twitter.anubhav.models.Block;
import com.twitter.anubhav.models.Props;
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
        Props expectedProps = new Props("p", 1);
        expectedBlock.addProperty(expectedProps);

        when(propertyParser.parseProp("p=1")).thenReturn(expectedProps);

        List<Block> blocks = configParser.parseBlocks(Stream.of("[block]", "p=1"));

        assertThat(blocks).hasSize(1);
        assertThat(blocks.get(0)).isEqualTo(expectedBlock);

    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void shouldReturnMultipleBlocksWithMultipleProps() {
        Block expectedBlock1 = new Block("block");
        Props expectedProps1 = new Props("p", 1);
        Props expectedProps2 = new Props("p", 1);
        expectedBlock1.addProperty(expectedProps1);
        expectedBlock1.addProperty(expectedProps2);

        Block expectedBlock2 = new Block("block1");
        Props expectedProps3 = new Props("p", 2);
        expectedBlock2.addProperty(expectedProps3);

        when(propertyParser.parseProp("p=1")).thenReturn(expectedProps1);
        when(propertyParser.parseProp("p=2")).thenReturn(expectedProps3);
        when(propertyParser.parseProp("p=4")).thenReturn(expectedProps2);

        List<Block> blocks = configParser.parseBlocks(Stream.of("[block]", "p=1", "p=4", "[block1]", "p=2"));

        assertThat(blocks).containsAll(List.of(expectedBlock1, expectedBlock2));

    }

}