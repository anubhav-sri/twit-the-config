package com.twitter.anubhav;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.File;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfigLoaderTest {

    @Mock
    private ConfigParser configParser;
    @Mock
    private ConfigFileReader configFileReader;

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void shouldParseAndReturnAllGroupNames() {
        String configFile = "/Users/anubhavsrivastava/code/twit-the-config/src/test/resources/config.conf";
        when(configParser.parseBlocks("[group1]")).thenReturn("group1");
        when(configParser.parseBlocks("[group2]")).thenReturn("group2");
        File file = new File(configFile);
        when(configFileReader.readLinesToStream(file)).thenReturn(Stream.of("[group1]", "[group2]"));


        Config config = new ConfigLoader(configParser, configFileReader)
                .loadConfig(configFile);

        verify(configFileReader).readLinesToStream(file);
        assertThat(config.get("group1")).isEqualTo("");
        assertThat(config.get("group2")).isEqualTo("");
    }

}
