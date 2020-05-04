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

        if (keyValueMatcher.matches()) {
            if (isAnOverRiddenProperty(keyValueMatcher)) {
                return createOverRiddenProp(keyValueMatcher);
            }
            return createDefaultProp(keyValueMatcher);
        }
        throw new ConfigFormatException("Not a correct format for key-value pair");

    }

    private Prop createDefaultProp(Matcher keyValueMatcher) {
        return new Prop(keyValueMatcher.group(1), keyValueMatcher.group(3));
    }

    private OverRidenProp createOverRiddenProp(Matcher keyValueMatcher) {
        return new OverRidenProp(keyValueMatcher.group(1),
                keyValueMatcher.group(3),
                keyValueMatcher.group(2));
    }

    private boolean isAnOverRiddenProperty(Matcher keyValueMatcher) {
        return !keyValueMatcher.group(2).isEmpty();
    }
}
