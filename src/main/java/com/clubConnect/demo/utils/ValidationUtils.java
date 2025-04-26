package com.clubConnect.demo.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final int MAX_EMAIL_LENGTH = 256; // Maximum length for emails
    private static final int MIN_PASSWORD_LENGTH = 10; // Minimum length for passwords
    private static final int MAX_PASSWORD_LENGTH = 512; // Maximum length for passwords
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^(?=.{1,255}$)(?!(.*\\.\\.)|.*\\.$)(?!(.*@.*@.*))" + // No consecutive dots, no multiple @s
            "[^\\s@.]+(\\.[^\\s@.]+)*@[^\\s@.]+(\\.[^\\s@.]+)+$" // Basic email format
    );

    public static boolean isValidEmail(String email) {
        if (email == null || email.length() < 5 || email.length() > MAX_EMAIL_LENGTH) {
            return false; // Length check
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            return false; // Length check
        }
        // Check for control characters
        for (char c : password.toCharArray()) {
            if (Character.isISOControl(c)) {
                return false; // Contains control characters
            }
        }
        return true; // Valid password
    }

    public static boolean isValidName(String name) {
        if (name == null || name.length() < 2 || name.length() > MAX_EMAIL_LENGTH) {
            return false; // Length check
        }
        return !name.trim().isEmpty() && !containsControlCharacters(name) && !containsConsecutiveDots(name);
    }

    public static boolean isValidClubName(String clubName) {
        return isValidName(clubName);
    }

    private static boolean containsControlCharacters(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isISOControl(c)) {
                return true; // Contains control characters
            }
        }
        return false;
    }

    private static boolean containsConsecutiveDots(String str) {
        return str.contains(".."); // Check for consecutive dots
    }
}