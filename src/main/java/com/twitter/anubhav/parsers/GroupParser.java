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
    private static final String REGEX_FOR_GROUP = "\\[([^]]+)]";
    private Pattern patternForGroup = Pattern.compile(REGEX_FOR_GROUP);

    public GroupParser(PropertyParser propertyParser) {
        this.propertyParser = propertyParser;
    }

    public List<Group> parseGroups(Stream<String> filteredLines) {
        List<Group> groupsInFile = new ArrayList<>();
        List<String> stringList = filteredLines.collect(Collectors.toList());

        String firstBlock = verifyIfItStartsWithBlock(stringList);
        Group currentGroup = new Group(firstBlock);

        for (String line : stringList.subList(1, stringList.size())) {
            Matcher matcherForGroup = patternForGroup.matcher(line);
            if (matcherForGroup.matches()) {
                currentGroup = addTheExistingGroupAndStartNewGroup(groupsInFile, currentGroup, matcherForGroup);
            } else {
                currentGroup.addProperty(propertyParser.parseProp(line));
            }
        }
        addTheLastGroupFound(groupsInFile, currentGroup);
        return groupsInFile;
    }

    private Group addTheExistingGroupAndStartNewGroup(List<Group> groupsInFile, Group currentGroup, Matcher matcherForGroup) {
        groupsInFile.add(currentGroup);
        currentGroup = new Group(matcherForGroup.group(1));
        return currentGroup;
    }

    private void addTheLastGroupFound(List<Group> groupsInFile, Group currentGroup) {
        if (!groupsInFile.contains(currentGroup)) groupsInFile.add(currentGroup);
    }

    private String verifyIfItStartsWithBlock(List<String> stringList) {
        Matcher compiledPattern = patternForGroup.matcher(stringList.get(0));
        if (compiledPattern.find()) {
            return compiledPattern.group(1);
        }
        throw new ConfigFormatException("No block found at start");
    }
}
