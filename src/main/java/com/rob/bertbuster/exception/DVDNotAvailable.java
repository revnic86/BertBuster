package com.rob.bertbuster.exception;

public class DVDNotAvailable extends RuntimeException {
    public DVDNotAvailable(String message) {
        super("No DVDs are available");
    }
}
