package com.twitter.anubhav.parsers;

import com.twitter.anubhav.exceptions.ConfigFormatException;
import com.twitter.anubhav.models.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupParser {
    private PropertyParser propertyParser;
    private static final String REGEX_FOR_BLOCK = "\\[([^]]+)\\]";
    private Pattern patternForBlocks = Pattern.compile(REGEX_FOR_BLOCK);

    public GroupParser(PropertyParser propertyParser) {
        this.propertyParser = propertyParser;
    }

    public String parseBlocks(String line) {
        if (line.matches("\\[([^]]+)\\]")) {
            return " Config()";
        }
        return "";
    }

    public List<Group> parseBlocks(Stream<String> filteredLines) {
        List<Group> groups = new ArrayList<>();
        List<String> stringList = filteredLines.collect(Collectors.toList());

        String firstBlock = verifyIfItStartsWithBlock(stringList);

        Group currentGroup = new Group(firstBlock);
        for (String line : stringList.subList(1, stringList.size())) {
            Matcher matcherForBlock = patternForBlocks.matcher(line);
            if (matcherForBlock.find()) {
                groups.add(currentGroup);
                currentGroup = new Group(matcherForBlock.group(1));
            } else {
                currentGroup.addProperty(propertyParser.parseProp(line));
            }
        }
        if (!groups.contains(currentGroup)) groups.add(currentGroup);
        return groups;
    }

    private String verifyIfItStartsWithBlock(List<String> stringList) {
        Matcher compiledPattern = patternForBlocks.matcher(stringList.get(0));
        if (compiledPattern.find()) {
            return compiledPattern.group(1);
        }
        throw new ConfigFormatException("No block found at start");
    }
}
