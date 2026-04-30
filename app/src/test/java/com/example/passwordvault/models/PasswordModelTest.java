package com.example.passwordvault.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordModelTest {

    @Test
    public void constructorShouldInitializeFieldsCorrectly() {
        PasswordModel model = new PasswordModel(
                1,
                "GitHub",
                "malak",
                "encryptedPwd",
                "github.com",
                "Personal account",
                "2026-04-30"
        );

        assertEquals(1, model.getId());
        assertEquals("GitHub", model.getServiceName());
        assertEquals("malak", model.getUsername());
        assertEquals("encryptedPwd", model.getPasswordEncrypted());
        assertEquals("github.com", model.getWebsite());
        assertEquals("Personal account", model.getNote());
        assertEquals("2026-04-30", model.getCreatedAt());
    }

    @Test
    public void settersShouldUpdateFieldsCorrectly() {
        PasswordModel model = new PasswordModel(
                0, "", "", "", "", "", ""
        );

        model.setId(5);
        model.setServiceName("LinkedIn");
        model.setUsername("user123");
        model.setPasswordEncrypted("encPass");
        model.setWebsite("linkedin.com");
        model.setNote("Professional");
        model.setCreatedAt("2026-05-01");

        assertEquals(5, model.getId());
        assertEquals("LinkedIn", model.getServiceName());
        assertEquals("user123", model.getUsername());
        assertEquals("encPass", model.getPasswordEncrypted());
        assertEquals("linkedin.com", model.getWebsite());
        assertEquals("Professional", model.getNote());
        assertEquals("2026-05-01", model.getCreatedAt());
    }
}