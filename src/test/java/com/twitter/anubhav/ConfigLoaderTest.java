package com.twitter.anubhav;

import com.twitter.anubhav.models.Block;
import com.twitter.anubhav.models.Props;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfigLoaderTest {

    @Mock
    private ConfigParser configParser;
    @Mock
    private ConfigFileReader configFileReader;

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void shouldParseAndReturnAllGroupNames() throws URISyntaxException {
        URI configFile = getFileURI("config.conf");

        Block block1 = new Block("group1");
        block1.addProperty(new Props("p", 1));

        Block block2 = new Block("group2");

        Config expectedConfig = new Config();
        expectedConfig.addBlock(block1);
        expectedConfig.addBlock(block2);

        Stream<String> streamOfLines = Stream.of("[group1]", "p=1", "[group2]");
        when(configFileReader.readLinesToStream(new File(configFile))).thenReturn(streamOfLines);
        when(configParser.parseBlocks(streamOfLines)).thenReturn(List.of(block1, block2));

        Config config = new ConfigLoader(configParser, configFileReader)
                .loadConfig(configFile.getPath());

        assertThat(config.get("group1").get("p")).isEqualTo(1);
        assertThat(config.get("group2")).isEqualTo(new HashMap<>());
    }

    private URI getFileURI(String fileName) throws URISyntaxException {
        return Objects.requireNonNull(this.getClass().getClassLoader().getResource(fileName)).toURI();
    }
}
