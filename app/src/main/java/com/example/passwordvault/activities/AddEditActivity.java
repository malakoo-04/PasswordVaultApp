package com.example.passwordvault.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.passwordvault.R;
import com.example.passwordvault.models.PasswordModel;
import com.example.passwordvault.repository.PasswordRepository;
import com.example.passwordvault.utils.SimpleCrypto;

public class AddEditActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etServiceName, etUsername, etPassword, etConfirmPassword, etWebsite, etNote;
    private ImageView ivTogglePassword, ivToggleConfirmPassword;
    private TextView btnGenerateInline, tvAddEditTitle;
    private Button btnCancel, btnSave;

    private boolean isPasswordVisible = false;
    private boolean isConfirmVisible = false;
    private int passwordId = -1;

    private PasswordRepository passwordRepository;

    private final ActivityResultLauncher<Intent> generatorLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String generated = result.getData().getStringExtra("generated_password");
                    if (generated != null && !generated.isEmpty()) {
                        etPassword.setText(generated);
                        etConfirmPassword.setText(generated);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        passwordRepository = new PasswordRepository(this);
        passwordId = getIntent().getIntExtra("password_id", -1);

        btnBack = findViewById(R.id.btnBack);
        tvAddEditTitle = findViewById(R.id.tvAddEditTitle);
        etServiceName = findViewById(R.id.etServiceName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etWebsite = findViewById(R.id.etWebsite);
        etNote = findViewById(R.id.etNote);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        ivToggleConfirmPassword = findViewById(R.id.ivToggleConfirmPassword);
        btnGenerateInline = findViewById(R.id.btnGenerateInline);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);

        if (passwordId != -1) {
            tvAddEditTitle.setText("Modifier le mot de passe");
            btnSave.setText("Enregistrer");
            loadPassword();
        }

        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());
        ivTogglePassword.setOnClickListener(v -> togglePassword());
        ivToggleConfirmPassword.setOnClickListener(v -> toggleConfirmPassword());
        btnGenerateInline.setOnClickListener(v -> generatorLauncher.launch(new Intent(this, GeneratorActivity.class).putExtra("return_mode", true)));
        btnSave.setOnClickListener(v -> saveEntry());
    }

    private void loadPassword() {
        PasswordModel model = passwordRepository.getPasswordById(passwordId);
        if (model == null) {
            Toast.makeText(this, "Entrée introuvable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etServiceName.setText(model.getServiceName());
        etUsername.setText(model.getUsername());
        String decryptedPassword = SimpleCrypto.decrypt(model.getPasswordEncrypted());
        etPassword.setText(decryptedPassword);
        etConfirmPassword.setText(decryptedPassword);
        etWebsite.setText(model.getWebsite());
        etNote.setText(model.getNote());
    }

    private void togglePassword() {
        if (isPasswordVisible) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isPasswordVisible = false;
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isPasswordVisible = true;
        }
        etPassword.setSelection(etPassword.getText().length());
    }

    private void toggleConfirmPassword() {
        if (isConfirmVisible) {
            etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isConfirmVisible = false;
        } else {
            etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isConfirmVisible = true;
        }
        etConfirmPassword.setSelection(etConfirmPassword.getText().length());
    }

    private void saveEntry() {
        String service = etServiceName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();
        String website = etWebsite.getText().toString().trim();
        String note = etNote.getText().toString().trim();

        if (service.isEmpty() || username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirm)) {
            Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
            return;
        }

        String encrypted = SimpleCrypto.encrypt(password);
        if (passwordId == -1) {
            passwordRepository.addPassword(service, username, encrypted, website, note);
            Toast.makeText(this, "Entrée ajoutée", Toast.LENGTH_SHORT).show();
        } else {
            passwordRepository.updatePassword(passwordId, service, username, encrypted, website, note);
            Toast.makeText(this, "Entrée mise à jour", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
