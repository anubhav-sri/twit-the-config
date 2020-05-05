package com.twitter.anubhav.readers;

import com.twitter.anubhav.exceptions.FileReadException;
import com.twitter.anubhav.exceptions.InvalidFileExtensionException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class ConfigFileReader {

    public Stream<String> readLinesToStream(File file) {
        if (!file.getName().endsWith(".conf")) throw new InvalidFileExtensionException("File extension should always be .conf");
        try {
            return Files.lines(file.toPath());
        } catch (IOException e) {
            throw new FileReadException(file.getName());
        }
    }
}
