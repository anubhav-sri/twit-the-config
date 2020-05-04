package com.twitter.anubhav.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

abstract class PatternMatchingParser<T, K> implements Parser<T, K> {
    private Pattern patternForGroup;

    PatternMatchingParser(String regex) {
        patternForGroup = Pattern.compile(regex);
    }

    Optional<List<String>> matches(String input) {
        Matcher matcher = patternForGroup.matcher(input);
        if (matcher.matches()) {
            return extractAllMatchingGroups(matcher);
        }
        return Optional.empty();

    }

    private Optional<List<String>> extractAllMatchingGroups(Matcher matcher) {
        ArrayList<String> matchedGroups = IntStream
                .rangeClosed(0, matcher.groupCount())
                .mapToObj(matcher::group)
                .collect(Collectors
                        .toCollection(ArrayList::new));
        return Optional.of(matchedGroups);
    }

}
