package com.example.passwordvault.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordGeneratorTest {

    @Test
    public void generatedPasswordShouldRespectRequestedLength() {
        String password = PasswordGenerator.generate(12, true, true, true, true);

        assertNotNull(password);
        assertEquals(12, password.length());
    }

    @Test
    public void generatedPasswordWithOnlyNumbersShouldContainOnlyDigits() {
        String password = PasswordGenerator.generate(20, false, false, true, false);

        assertTrue(password.matches("[0-9]+"));
    }

    @Test
    public void generatedPasswordWithOnlyUppercaseShouldContainOnlyUppercaseLetters() {
        String password = PasswordGenerator.generate(15, true, false, false, false);

        assertTrue(password.matches("[A-Z]+"));
    }

    @Test
    public void generatedPasswordShouldFallbackToDefaultCharsetIfNoOptionSelected() {
        String password = PasswordGenerator.generate(10, false, false, false, false);

        assertNotNull(password);
        assertEquals(10, password.length());
    }
}