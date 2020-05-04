package com.twitter.anubhav.parsers;

import com.twitter.anubhav.exceptions.ConfigFormatException;
import com.twitter.anubhav.models.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class GroupParser extends PatternMatchingParser<Stream<String>, List<Group>> {
    private static final String GROUP_REGEX = "\\[([^]]+)]";
    private PropertyParser propertyParser;

    public GroupParser(PropertyParser propertyParser) {
        super(GROUP_REGEX);
        this.propertyParser = propertyParser;
    }

    public List<Group> parse(Stream<String> filteredLines) {
        List<Group> groupsInFile = new ArrayList<>();

        var currentGroupRef = new Object() {
            Group currentGroup = null;
        };

        filteredLines.forEach(line -> {
            Optional<List<String>> matchedTokens = super.matches(line);
            if (matchedTokens.isPresent()) {
                currentGroupRef.currentGroup = addTheExistingGroupAndStartNewGroup(groupsInFile, currentGroupRef.currentGroup, matchedTokens.get());
            } else {
                verifyIfPropsHaveAGroup(currentGroupRef.currentGroup);
                currentGroupRef.currentGroup.addProperty(propertyParser.parse(line));
            }
        });
        groupsInFile.add(currentGroupRef.currentGroup);
        return groupsInFile;
    }

    private void verifyIfPropsHaveAGroup(Group currentGroup) {
        if (currentGroup == null) throw new ConfigFormatException("No block found at start");
    }

    private Group addTheExistingGroupAndStartNewGroup(List<Group> groupsInFile, Group currentGroup, List<String> matcherForGroup) {
        if (currentGroup != null)
            groupsInFile.add(currentGroup);
        return new Group(matcherForGroup.get(1));
    }
}
