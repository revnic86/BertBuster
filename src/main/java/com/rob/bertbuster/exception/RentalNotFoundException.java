package com.rob.bertbuster.exception;

import java.util.UUID;

public class RentalNotFoundException extends RuntimeException {
    public RentalNotFoundException(UUID uuid) {
        super("rental id - " + uuid + "was not found");
    }
}
