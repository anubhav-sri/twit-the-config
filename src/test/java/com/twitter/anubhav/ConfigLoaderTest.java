package com.twitter.anubhav;

import com.twitter.anubhav.dto.Config;
import com.twitter.anubhav.models.Group;
import com.twitter.anubhav.models.Prop;
import com.twitter.anubhav.parsers.GroupParser;
import com.twitter.anubhav.readers.ConfigFileReader;
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
    private GroupParser groupParser;
    @Mock
    private ConfigFileReader configFileReader;

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void shouldParseAndReturnAllGroupNames() throws URISyntaxException {
        URI configFile = getFileURI();

        Group group1 = new Group("group1");
        group1.addProperty(new Prop("p", 1));

        Group group2 = new Group("group2");

        Config expectedConfig = new Config();
        expectedConfig.addBlock(group1);
        expectedConfig.addBlock(group2);

        Stream<String> streamOfLines = Stream.of("[group1]", "p=1", "[group2]");
        when(configFileReader.readLinesToStream(new File(configFile))).thenReturn(streamOfLines);
        when(groupParser.parseBlocks(streamOfLines)).thenReturn(List.of(group1, group2));

        Config config = new ConfigLoader(groupParser, configFileReader)
                .loadConfig(configFile.getPath());

        assertThat(config.get("group1").get("p")).isEqualTo(1);
        assertThat(config.get("group2")).isEqualTo(new HashMap<>());
    }

    private URI getFileURI() throws URISyntaxException {
        return Objects.requireNonNull(this.getClass().getClassLoader().getResource("config.conf")).toURI();
    }
}
