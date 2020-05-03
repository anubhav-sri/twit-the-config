package com.twitter.anubhav;


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
        configFileReader.readLinesToStream(new File(configFile))
                .map(line -> configParser.parseBlocks(line))
                .forEach(config::add);
        return config;
    }

}