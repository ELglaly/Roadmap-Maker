package com.roadmap.backendapi.utils;


public class Const {

        // Fixed typo: BARSER -> BEARER
        public static final String BEARER = "Bearer ";

        public static final class PasswordErrorMessages {
            public static final String PASSWORD_TOO_SHORT = "Password must be at least 8 characters long";
            public static final String PASSWORD_REQUIRED = "Password is required";
            public static final String PASSWORD_MISMATCH = "Passwords do not match";
            public static final String PASSWORD_INCORRECT = "Password is incorrect";
            public static final String PASSWORD_CONSTRAINTS = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespace";

            private PasswordErrorMessages() {
                throw new UnsupportedOperationException("Utility class");
            }
        }

        public static final class RegularExpressions {
            public static final String STRING_PATTERN = "^[A-Za-z\\s'-]{2,50}$";
            public static final String STRING_PATTERN_ERROR = "must be 2-50 characters, letters, spaces, hyphens and apostrophes only";
            public static final String ZIP_PATTERN = "^[A-Z0-9\\s-]{3,15}$"; // Fixed: ZIB -> ZIP
            public static final String ZIP_PATTERN_ERROR = "ZIP code must be 3-15 characters, alphanumeric with spaces and hyphens";

            private RegularExpressions() {
                throw new UnsupportedOperationException("Utility class");
            }
        }

        private Const() {
            throw new UnsupportedOperationException("Utility class");
        }
}
