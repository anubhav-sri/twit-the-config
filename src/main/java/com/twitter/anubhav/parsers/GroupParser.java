package com.twitter.anubhav.parsers;

import com.twitter.anubhav.exceptions.ConfigFormatException;
import com.twitter.anubhav.models.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

        var currentGroupRef = new Object() {
            Group currentGroup = null;
        };

        filteredLines.forEach(line -> {
            Matcher matcherForGroup = patternForGroup.matcher(line);
            if (matcherForGroup.matches()) {
                currentGroupRef.currentGroup = addTheExistingGroupAndStartNewGroup(groupsInFile, currentGroupRef.currentGroup, matcherForGroup);
            } else {
                verifyIfPropsHaveAGroup(currentGroupRef.currentGroup);
                currentGroupRef.currentGroup.addProperty(propertyParser.parseProp(line));
            }
        });
        groupsInFile.add(currentGroupRef.currentGroup);
        return groupsInFile;
    }

    private void verifyIfPropsHaveAGroup(Group currentGroup) {
        if (currentGroup == null) throw new ConfigFormatException("No block found at start");
    }

    private Group addTheExistingGroupAndStartNewGroup(List<Group> groupsInFile, Group currentGroup, Matcher matcherForGroup) {
        groupsInFile.add(currentGroup);
        return new Group(matcherForGroup.group(1));
    }

}
