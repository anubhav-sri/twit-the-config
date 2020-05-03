package com.twitter.anubhav.exceptions;

public class FileReadException extends RuntimeException {
    public FileReadException(String fileName) {
        super("Unable to read the file with name " + fileName);
    }
}
