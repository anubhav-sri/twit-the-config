package com.twitter.anubhav.parsers;

import com.twitter.anubhav.exceptions.ConfigFormatException;
import com.twitter.anubhav.models.OverRidenProp;
import com.twitter.anubhav.models.Prop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyParser {
    private static final String REGEX_FOR_KEY_VALUE_PAIRS = "([a-zA-Z_ ]+)<*([a-zA-Z]*)>*=([a-zA-Z0-9\\-_ ]*)";
    private Pattern patternForKeyValuePairs = Pattern.compile(REGEX_FOR_KEY_VALUE_PAIRS);

    public Prop parseProp(String line) {
        Matcher keyValueMatcher = patternForKeyValuePairs.matcher(line);
        if (keyValueMatcher.find()) {
            if (!keyValueMatcher.group(2).isEmpty()) {
                return new OverRidenProp(keyValueMatcher.group(1),
                        keyValueMatcher.group(3),
                        keyValueMatcher.group(2));
            }
            return new Prop(keyValueMatcher.group(1), keyValueMatcher.group(3));
        }
        throw new ConfigFormatException("Not a correct format for key-value pair");

    }
}
