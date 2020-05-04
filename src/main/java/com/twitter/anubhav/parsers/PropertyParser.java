package com.twitter.anubhav.parsers;

import com.twitter.anubhav.exceptions.ConfigFormatException;
import com.twitter.anubhav.models.Props;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyParser {
    private static final String REGEX_FOR_KEY_VALUE_PAIRS = "([a-zA-Z_ ]*[<a-zA-Z_ >]*)=([a-zA-Z0-9\\- ]*)";
    private Pattern patternForKeyValuePairs = Pattern.compile(REGEX_FOR_KEY_VALUE_PAIRS);

    public Props parseProp(String line) {
        Matcher keyValueMatcher = patternForKeyValuePairs.matcher(line);
        if (keyValueMatcher.find()) {
            return new Props(keyValueMatcher.group(1), keyValueMatcher.group(2));
        }
        throw new ConfigFormatException("Not a correct format for key-value pair");

    }
}
