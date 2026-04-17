package com.example.passwordvault.repository;

import android.content.Context;

import com.example.passwordvault.database.DatabaseHelper;
import com.example.passwordvault.models.PasswordModel;

import java.util.ArrayList;

public class PasswordRepository {
    private final DatabaseHelper databaseHelper;

    public PasswordRepository(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public ArrayList<PasswordModel> getAllPasswords() {
        return databaseHelper.getAllPasswords();
    }

    public ArrayList<PasswordModel> searchPasswords(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllPasswords();
        }
        return databaseHelper.searchPasswords(query.trim());
    }

    public PasswordModel getPasswordById(int id) {
        return databaseHelper.getPassword(id);
    }

    public long addPassword(String serviceName, String username, String encryptedPassword,
                            String website, String note) {
        return databaseHelper.insertPassword(serviceName, username, encryptedPassword, website, note);
    }

    public int updatePassword(int id, String serviceName, String username, String encryptedPassword,
                              String website, String note) {
        return databaseHelper.updatePassword(id, serviceName, username, encryptedPassword, website, note);
    }

    public void deletePassword(int id) {
        databaseHelper.deletePassword(id);
    }
}
