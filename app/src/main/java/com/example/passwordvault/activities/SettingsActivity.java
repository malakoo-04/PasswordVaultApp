package com.example.passwordvault.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.passwordvault.R;
import com.example.passwordvault.repository.SessionManager;
import com.example.passwordvault.utils.BiometricHelper;

public class SettingsActivity extends AppCompatActivity {

    private ImageView btnBackSettings;

    private boolean updatingBiometricSwitch = false;
    private LinearLayout rowChangeMasterPassword, rowExport, rowImport, rowLogout;
    private Switch switchBiometric, switchDarkMode;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sessionManager = new SessionManager(this);

        btnBackSettings = findViewById(R.id.btnBackSettings);
        rowChangeMasterPassword = findViewById(R.id.rowChangeMasterPassword);
        rowExport = findViewById(R.id.rowExport);
        rowImport = findViewById(R.id.rowImport);
        rowLogout = findViewById(R.id.rowLogout);
        switchBiometric = findViewById(R.id.switchBiometric);
        switchDarkMode = findViewById(R.id.switchDarkMode);

        switchBiometric.setChecked(sessionManager.isBiometricEnabled());
        switchDarkMode.setChecked(sessionManager.isDarkModeEnabled());

        btnBackSettings.setOnClickListener(v -> finish());
        rowChangeMasterPassword.setOnClickListener(v -> showChangePasswordDialog());
        rowExport.setOnClickListener(v -> Toast.makeText(this, "Export à ajouter plus tard", Toast.LENGTH_SHORT).show());
        rowImport.setOnClickListener(v -> Toast.makeText(this, "Import à ajouter plus tard", Toast.LENGTH_SHORT).show());

        switchBiometric.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (updatingBiometricSwitch) return;

            if (isChecked) {
                if (!BiometricHelper.isBiometricAvailable(this)) {
                    updatingBiometricSwitch = true;
                    switchBiometric.setChecked(false);
                    updatingBiometricSwitch = false;
                    sessionManager.setBiometricEnabled(false);
                    Toast.makeText(this, "Biométrie non disponible sur cet appareil", Toast.LENGTH_SHORT).show();
                    return;
                }

                java.util.concurrent.Executor executor = androidx.core.content.ContextCompat.getMainExecutor(this);

                BiometricHelper.showBiometricPrompt(this, executor, new BiometricHelper.AuthCallback() {
                    @Override
                    public void onSuccess() {
                        sessionManager.setBiometricEnabled(true);
                        Toast.makeText(SettingsActivity.this, "Biométrie activée", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String message) {
                        updatingBiometricSwitch = true;
                        switchBiometric.setChecked(false);
                        updatingBiometricSwitch = false;
                        sessionManager.setBiometricEnabled(false);
                        Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                sessionManager.setBiometricEnabled(false);
                Toast.makeText(this, "Biométrie désactivée", Toast.LENGTH_SHORT).show();
            }
        });

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sessionManager.setDarkModeEnabled(isChecked);
            Toast.makeText(this, "Thème sombre " + (isChecked ? "activé" : "désactivé"), Toast.LENGTH_SHORT).show();
        });

        rowLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Toast.makeText(this, "Déconnecté", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            finishAffinity();
        });
    }

    private void showChangePasswordDialog() {
        final EditText input = new EditText(this);
        input.setHint("Nouveau mot de passe maître");
        input.setPadding(32, 24, 32, 24);

        new AlertDialog.Builder(this)
                .setTitle("Changer le mot de passe maître")
                .setView(input)
                .setPositiveButton("Enregistrer", (dialog, which) -> {
                    String newPassword = input.getText().toString().trim();
                    if (newPassword.isEmpty()) {
                        Toast.makeText(this, "Mot de passe vide", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sessionManager.saveMasterPassword(newPassword);
                    Toast.makeText(this, "Mot de passe maître mis à jour", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}
