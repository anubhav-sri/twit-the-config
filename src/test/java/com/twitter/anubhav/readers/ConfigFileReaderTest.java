package com.twitter.anubhav.readers;

import com.twitter.anubhav.exceptions.FileReadException;
import com.twitter.anubhav.exceptions.InvalidFileExtensionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigFileReaderTest {

    private ConfigFileReader configFileReader;

    @BeforeEach
    void setUp() {
        configFileReader = new ConfigFileReader();
    }

    @Test
    void shouldReadFilesToStreamOfLines() throws URISyntaxException {
        File inputFile = getFileURI("config.conf");

        Stream<String> streamOfLines = new ConfigFileReader().readLinesToStream(inputFile);

        assertThat(streamOfLines).containsAll(List.of("[group1]", "[group2]"));
    }

    @Test
    void shouldThrowInvalidFileExtensionExceptionIfFileWithInvalidExtensionPasses() throws URISyntaxException {
        File invalidInputFile = getFileURI("config.invalid");

        Assertions.assertThrows(InvalidFileExtensionException.class, () -> configFileReader.readLinesToStream(invalidInputFile));
    }

    @Test
    void shouldThrowFileReadExceptionIfFileWithInValidPath() {
        File invalidInputFile = new File("/invalid/conf.conf");

        Assertions.assertThrows(FileReadException.class, () -> configFileReader.readLinesToStream(invalidInputFile));
    }

    private File getFileURI(String fileName) throws URISyntaxException {
        return new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource(fileName)).toURI());
    }

}