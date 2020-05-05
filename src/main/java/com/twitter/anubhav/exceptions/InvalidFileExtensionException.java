package com.twitter.anubhav.exceptions;

public class InvalidFileExtensionException extends RuntimeException {
    public InvalidFileExtensionException(String message) {
        super(message);
    }
}
