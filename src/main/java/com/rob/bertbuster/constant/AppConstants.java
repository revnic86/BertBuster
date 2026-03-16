package com.rob.bertbuster.constant;

public final class AppConstants {

    private AppConstants(){

    }

    // Admin account
    public static final String ADMIN_USER = "admin";
    public static final String ADMIN_PASS = "admin123";  // just for demo purpose
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String ADMIN_SUCCESS = "Admin account created successfully!";


    // Validation
    public static final String NO_TITLE = "You must enter a title";
    public static final String INVALID_TITLE_SIZE = "Title must be between 1 and 250 characters";
    public static final String NO_RELEASE_YEAR = "You must include a release year";
    public static final String NO_RUNTIME = "You must enter a runtime";
    public static final String NO_DVD_BARCODE = "DVD barcode cannot be blank";
    public static final String NO_DVD_MOVIE = "DVD must be associated with a movie";

    public static final String RENTAL_SUCCESS = "Rental created successfully!";


}
