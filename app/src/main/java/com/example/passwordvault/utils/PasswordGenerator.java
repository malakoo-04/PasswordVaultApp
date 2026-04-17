package com.example.passwordvault.utils;
import java.security.SecureRandom;

public class PasswordGenerator {
    public static String generate(int length, boolean upper, boolean lower, boolean numbers, boolean symbols) {
        String chars = "";
        if (upper) chars += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (lower) chars += "abcdefghijklmnopqrstuvwxyz";
        if (numbers) chars += "0123456789";
        if (symbols) chars += "!@#$%^&*()-_=+[]{}?";
        if (chars.isEmpty()) chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<length;i++) sb.append(chars.charAt(random.nextInt(chars.length())));
        return sb.toString();
    }
}
