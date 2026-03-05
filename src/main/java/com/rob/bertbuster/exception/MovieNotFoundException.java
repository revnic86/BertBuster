package com.rob.bertbuster.exception;

import java.util.UUID;

public class MovieNotFoundException extends RuntimeException {

    public MovieNotFoundException(UUID uuid) {
        super("Movie with " + uuid + "was not found");
    }
}
