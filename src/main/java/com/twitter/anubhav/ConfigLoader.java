package com.twitter.anubhav;


import com.twitter.anubhav.dto.Config;
import com.twitter.anubhav.parsers.GroupParser;
import com.twitter.anubhav.readers.ConfigFileReader;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ConfigLoader {
    private static final Predicate<String> NON_EMPTY_FILTER = s -> !s.trim().isEmpty();
    private GroupParser groupParser;
    private ConfigFileReader configFileReader;

    public ConfigLoader(GroupParser groupParser, ConfigFileReader configFileReader) {
        this.groupParser = groupParser;
        this.configFileReader = configFileReader;
    }

    public Config loadConfig(String configFile, List<String> overrides) {
        Config config = new Config();

        Stream<String> nonEmptyLinesInFile = readNonEmptyLinesFromFile(configFile);

        parseAndEnrichTheConfig(overrides, config, nonEmptyLinesInFile);
        return config;
    }

    private void parseAndEnrichTheConfig(List<String> overrides, Config config, Stream<String> nonEmptyLinesInFile) {
        groupParser
                .parse(nonEmptyLinesInFile)
                .forEach(group -> config.addBlock(group, overrides));
    }

    private Stream<String> readNonEmptyLinesFromFile(String configFile) {
        return configFileReader
                .readLinesToStream(new File(configFile))
                .filter(NON_EMPTY_FILTER);
    }

}