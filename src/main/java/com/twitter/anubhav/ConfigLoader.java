package com.twitter.anubhav;


import com.twitter.anubhav.dto.Config;
import com.twitter.anubhav.parsers.GroupParser;
import com.twitter.anubhav.readers.ConfigFileReader;

import java.io.File;

public class ConfigLoader {
    private GroupParser groupParser;
    private ConfigFileReader configFileReader;

    public ConfigLoader(GroupParser groupParser, ConfigFileReader configFileReader) {
        this.groupParser = groupParser;
        this.configFileReader = configFileReader;
    }

    public Config loadConfig(String configFile) {
        Config config = new Config();
        groupParser.parseGroups(configFileReader.readLinesToStream(new File(configFile)))
                .forEach(config::addBlock);
        return config;
    }

}