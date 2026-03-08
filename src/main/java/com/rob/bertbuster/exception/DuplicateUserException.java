package com.rob.bertbuster.exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message) {

        super("User already exists");
    }
}
