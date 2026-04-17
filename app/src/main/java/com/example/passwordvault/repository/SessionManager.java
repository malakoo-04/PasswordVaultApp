package com.example.passwordvault.repository;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREFS_NAME = "vault_prefs";
    private static final String KEY_MASTER_PASSWORD = "master_password";
    private static final String KEY_LOGGED_IN = "is_logged_in";
    private static final String KEY_BIOMETRIC = "biometric_enabled";
    private static final String KEY_DARK_MODE = "dark_mode";

    private final SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean hasMasterPassword() {
        String password = sharedPreferences.getString(KEY_MASTER_PASSWORD, null);
        return password != null && !password.isEmpty();
    }

    public void saveMasterPassword(String password) {
        sharedPreferences.edit().putString(KEY_MASTER_PASSWORD, password).apply();
    }

    public String getMasterPassword() {
        return sharedPreferences.getString(KEY_MASTER_PASSWORD, null);
    }

    public boolean checkMasterPassword(String password) {
        String savedPassword = getMasterPassword();
        return savedPassword != null && savedPassword.equals(password);
    }

    public void setLoggedIn(boolean loggedIn) {
        sharedPreferences.edit().putBoolean(KEY_LOGGED_IN, loggedIn).apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false);
    }

    public void logout() {
        sharedPreferences.edit().putBoolean(KEY_LOGGED_IN, false).apply();
    }

    public void clearAll() {
        sharedPreferences.edit().clear().apply();
    }

    public void setBiometricEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(KEY_BIOMETRIC, enabled).apply();
    }

    public boolean isBiometricEnabled() {
        return sharedPreferences.getBoolean(KEY_BIOMETRIC, false);
    }

    public void setDarkModeEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }

    public boolean isDarkModeEnabled() {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false);
    }
}
