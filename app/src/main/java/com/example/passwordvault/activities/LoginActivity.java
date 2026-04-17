package com.example.passwordvault.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.Executor;
import androidx.core.content.ContextCompat;
import com.example.passwordvault.utils.BiometricHelper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passwordvault.R;
import com.example.passwordvault.repository.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etMasterPassword;
    private ImageView ivTogglePassword;
    private Button btnContinue;

    private Button btnBiometricLogin;
    private TextView tvLoginTitle;
    private TextView tvLoginSubtitle;

    private boolean isPasswordVisible = false;
    private SessionManager sessionManager;
    private boolean isFirstSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        isFirstSetup = !sessionManager.hasMasterPassword();

        etMasterPassword = findViewById(R.id.etMasterPassword);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        btnContinue = findViewById(R.id.btnContinue);
        tvLoginTitle = findViewById(R.id.tvLoginTitle);
        tvLoginSubtitle = findViewById(R.id.tvLoginSubtitle);
        btnBiometricLogin = findViewById(R.id.btnBiometricLogin);

        setupScreenMode();
        updateBiometricButtonVisibility();

        ivTogglePassword.setOnClickListener(v -> togglePasswordVisibility());
        btnContinue.setOnClickListener(v -> handleContinue());
        btnBiometricLogin.setOnClickListener(v -> launchBiometricAuth());

    }

    private void setupScreenMode() {
        if (isFirstSetup) {
            tvLoginTitle.setText("Créer un mot de passe maître");
            tvLoginSubtitle.setText("Créez un mot de passe maître pour sécuriser vos données");
            btnContinue.setText("Créer et continuer");
        } else {
            tvLoginTitle.setText("Déverrouiller l'application");
            tvLoginSubtitle.setText("Entrez votre mot de passe maître pour continuer");
            btnContinue.setText("Se connecter");
        }
    }

    private void handleContinue() {
        String password = etMasterPassword.getText().toString().trim();

        if (password.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un mot de passe", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isFirstSetup) {
            sessionManager.saveMasterPassword(password);
            sessionManager.setLoggedIn(true);
            Toast.makeText(this, "Mot de passe maître enregistré", Toast.LENGTH_SHORT).show();
            openMainScreen();
            return;
        }

        if (sessionManager.checkMasterPassword(password)) {
            sessionManager.setLoggedIn(true);
            Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show();
            openMainScreen();
        } else {
            Toast.makeText(this, "Mot de passe incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    private void openMainScreen() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            etMasterPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isPasswordVisible = false;
        } else {
            etMasterPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isPasswordVisible = true;
        }
        etMasterPassword.setSelection(etMasterPassword.getText().length());
    }

    private void updateBiometricButtonVisibility() {
        boolean showButton = !isFirstSetup
                && sessionManager.isBiometricEnabled()
                && BiometricHelper.isBiometricAvailable(this);

        btnBiometricLogin.setVisibility(showButton ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    private void launchBiometricAuth() {
        Executor executor = ContextCompat.getMainExecutor(this);

        BiometricHelper.showBiometricPrompt(this, executor, new BiometricHelper.AuthCallback() {
            @Override
            public void onSuccess() {
                sessionManager.setLoggedIn(true);
                Toast.makeText(LoginActivity.this, "Connexion biométrique réussie", Toast.LENGTH_SHORT).show();
                openMainScreen();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
