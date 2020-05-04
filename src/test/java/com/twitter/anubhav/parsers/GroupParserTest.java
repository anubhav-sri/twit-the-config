package com.twitter.anubhav.parsers;

import com.twitter.anubhav.exceptions.ConfigFormatException;
import com.twitter.anubhav.models.Group;
import com.twitter.anubhav.models.Prop;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupParserTest {

    private GroupParser groupParser;
    @Mock
    private PropertyParser propertyParser;

    @BeforeEach
    void setUp() {
        groupParser = new GroupParser(propertyParser);
    }

    @Test
    void shouldThrowConfigFormatExceptionWhenFileDoesNotStartsWithBlock() {
        Assertions.assertThrows(ConfigFormatException.class,
                () -> groupParser.parseGroups(Stream.of("p=1", "[block]")));

    }

    @Test
    void shouldReturnBlockWithEmptyProps() {
        List<Group> groups = groupParser.parseGroups(Stream.of("[block]"));
        assertThat(groups).hasSize(1);
        assertThat(groups.get(0)).isEqualTo(new Group("block"));

    }

    @Test
    void shouldReturnBlockWithProps() {
        Group expectedGroup = new Group("block");
        Prop expectedProp = new Prop("p", 1);
        expectedGroup.addProperty(expectedProp);

        when(propertyParser.parseProp("p=1")).thenReturn(expectedProp);

        List<Group> groups = groupParser.parseGroups(Stream.of("[block]", "p=1"));

        assertThat(groups).hasSize(1);
        assertThat(groups.get(0)).isEqualTo(expectedGroup);

    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void shouldReturnMultipleBlocksWithMultipleProps() {
        Group expectedGroup1 = new Group("block");
        Prop expectedProp1 = new Prop("p", 1);
        Prop expectedProp2 = new Prop("p", 1);
        expectedGroup1.addProperty(expectedProp1);
        expectedGroup1.addProperty(expectedProp2);

        Group expectedGroup2 = new Group("block1");
        Prop expectedProp3 = new Prop("p", 2);
        expectedGroup2.addProperty(expectedProp3);

        when(propertyParser.parseProp("p=1")).thenReturn(expectedProp1);
        when(propertyParser.parseProp("p=2")).thenReturn(expectedProp3);
        when(propertyParser.parseProp("p=4")).thenReturn(expectedProp2);

        List<Group> groups = groupParser.parseGroups(Stream.of("[block]", "p=1", "p=4", "[block1]", "p=2"));

        assertThat(groups).containsAll(List.of(expectedGroup1, expectedGroup2));

    }

}