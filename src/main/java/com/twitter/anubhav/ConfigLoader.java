package com.twitter.anubhav;


import com.twitter.anubhav.dto.Config;
import com.twitter.anubhav.parsers.ConfigParser;
import com.twitter.anubhav.readers.ConfigFileReader;

import java.io.File;

public class ConfigLoader {
    private ConfigParser configParser;
    private ConfigFileReader configFileReader;

    public ConfigLoader(ConfigParser configParser, ConfigFileReader configFileReader) {
        this.configParser = configParser;
        this.configFileReader = configFileReader;
    }

    public Config loadConfig(String configFile) {
        Config config = new Config();
        configParser.parseBlocks(configFileReader.readLinesToStream(new File(configFile)))
                .forEach(config::addBlock);
        return config;
    }

}