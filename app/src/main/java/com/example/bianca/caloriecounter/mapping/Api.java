package com.example.bianca.caloriecounter.mapping;

/**
 * Created by bianca on 30.11.2016.
 */

public class Api {
    public static class Note {
        public static final String URL = "api/aliment";
        public static final String ALIMENT_CREATED = "aliment/created";
        public static final String ALIMENT_UPDATED = "aliment/updated";
        public static final String ALIMENT_DELETED = "aliment/deleted";
        public static final String NAME = "name";
        public static final String CALORIES = "calories";
        public static final String PROTEINS = "proteins";
        public static final String FATS = "fats";
        public static final String CARBS = "carbs";
    }
    public static class Auth {
        public static final String TOKEN = "token";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
    }
}
