package com.twitter.anubhav;

import com.twitter.anubhav.exceptions.ConfigFormatException;
import com.twitter.anubhav.models.Block;
import com.twitter.anubhav.parsers.PropertyParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigParser {
    private PropertyParser propertyParser;
    private static final String REGEX_FOR_BLOCK = "\\[([^]]+)\\]";
    private Pattern patternForBlocks = Pattern.compile(REGEX_FOR_BLOCK);

    public ConfigParser(PropertyParser propertyParser) {
        this.propertyParser = propertyParser;
    }

    public String parseBlocks(String line) {
        if (line.matches("\\[([^]]+)\\]")) {
            return " Config()";
        }
        return "";
    }

    public List<Block> parseBlocks(Stream<String> filteredLines) {
        List<Block> blocks = new ArrayList<>();
        List<String> stringList = filteredLines.collect(Collectors.toList());

        String firstBlock = verifyIfItStartsWithBlock(stringList);

        Block currentBlock = new Block(firstBlock);
        for (String line : stringList.subList(1, stringList.size())) {
            Matcher matcherForBlock = patternForBlocks.matcher(line);
            if (matcherForBlock.find()) {
                blocks.add(currentBlock);
                currentBlock = new Block(matcherForBlock.group(1));
            } else {
                currentBlock.addProperty(propertyParser.parseProp(line));
            }
        }
        if (!blocks.contains(currentBlock)) blocks.add(currentBlock);
        return blocks;
    }

    private String verifyIfItStartsWithBlock(List<String> stringList) {
        Matcher compiledPattern = patternForBlocks.matcher(stringList.get(0));
        if (compiledPattern.find()) {
            return compiledPattern.group(1);
        }
        throw new ConfigFormatException("No block found at start");
    }
}
