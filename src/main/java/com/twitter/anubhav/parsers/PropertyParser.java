package com.twitter.anubhav.parsers;

import com.twitter.anubhav.exceptions.ConfigFormatException;
import com.twitter.anubhav.models.OverRiddenProp;
import com.twitter.anubhav.models.Prop;

import java.util.List;
import java.util.Optional;

public class PropertyParser extends PatternMatchingParser<String, Prop> {
    private static final String REGEX_FOR_KEY_VALUE_PAIRS = "([a-zA-Z_ ]+)<*([a-zA-Z]*)>*=([a-zA-Z0-9\\-_ ]*)";

    public PropertyParser() {
        super(REGEX_FOR_KEY_VALUE_PAIRS);
    }

    public Prop parse(String line) {

        Optional<List<String>> matchedTokens = super.matches(line);
        if (matchedTokens.isPresent()) {
            if (isAnOverRiddenProperty(matchedTokens.get())) {
                return createOverRiddenProp(matchedTokens.get());
            }
            return createDefaultProp(matchedTokens.get());
        }
        throw new ConfigFormatException("Not a correct format for key-value pair");

    }

    private Prop createDefaultProp(List<String> keyValueMatcher) {
        return new Prop(keyValueMatcher.get(1), keyValueMatcher.get(3));
    }

    private OverRiddenProp createOverRiddenProp(List<String> keyValueMatcher) {
        return new OverRiddenProp(keyValueMatcher.get(1),
                keyValueMatcher.get(3),
                keyValueMatcher.get(2));
    }

    private boolean isAnOverRiddenProperty(List<String> keyValueMatcher) {
        return !keyValueMatcher.get(2).isEmpty();
    }
}
