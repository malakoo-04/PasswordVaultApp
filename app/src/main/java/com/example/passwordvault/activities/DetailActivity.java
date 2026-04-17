package com.example.passwordvault.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.passwordvault.R;
import com.example.passwordvault.models.PasswordModel;
import com.example.passwordvault.repository.PasswordRepository;
import com.example.passwordvault.utils.SimpleCrypto;

public class DetailActivity extends AppCompatActivity {

    private ImageView btnBackDetail, ivToggleDetailPassword;
    private TextView tvDetailServiceName, tvDetailUsername, btnCopyPassword, tvDetailPassword, tvDetailWebsite, tvDetailNote;
    private Button btnEditEntry, btnDeleteEntry;

    private boolean isPasswordVisible = false;
    private String realPassword = "";
    private int passwordId = -1;
    private PasswordRepository passwordRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        passwordRepository = new PasswordRepository(this);
        passwordId = getIntent().getIntExtra("password_id", -1);

        btnBackDetail = findViewById(R.id.btnBackDetail);
        ivToggleDetailPassword = findViewById(R.id.ivToggleDetailPassword);
        tvDetailServiceName = findViewById(R.id.tvDetailServiceName);
        tvDetailUsername = findViewById(R.id.tvDetailUsername);
        btnCopyPassword = findViewById(R.id.btnCopyPassword);
        tvDetailPassword = findViewById(R.id.tvDetailPassword);
        tvDetailWebsite = findViewById(R.id.tvDetailWebsite);
        tvDetailNote = findViewById(R.id.tvDetailNote);
        btnEditEntry = findViewById(R.id.btnEditEntry);
        btnDeleteEntry = findViewById(R.id.btnDeleteEntry);

        loadPassword();

        btnBackDetail.setOnClickListener(v -> finish());
        ivToggleDetailPassword.setOnClickListener(v -> togglePassword());

        btnCopyPassword.setOnClickListener(v -> {
            if (realPassword.isEmpty()) return;
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("password", realPassword);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Mot de passe copié", Toast.LENGTH_SHORT).show();
        });

        btnEditEntry.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, AddEditActivity.class);
            intent.putExtra("password_id", passwordId);
            startActivity(intent);
        });

        tvDetailWebsite.setOnClickListener(v -> {
            String url = tvDetailWebsite.getText().toString().trim();

            if (url.isEmpty() || url.equals("—")) {
                Toast.makeText(this, "Aucun site disponible", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Impossible d'ouvrir le lien", Toast.LENGTH_SHORT).show();
            }
        });

        btnDeleteEntry.setOnClickListener(v -> confirmDelete());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPassword();
    }

    private void loadPassword() {
        if (passwordId == -1) {
            Toast.makeText(this, "Entrée introuvable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        PasswordModel model = passwordRepository.getPasswordById(passwordId);
        if (model == null) {
            Toast.makeText(this, "Entrée introuvable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        realPassword = SimpleCrypto.decrypt(model.getPasswordEncrypted());
        tvDetailServiceName.setText(model.getServiceName());
        tvDetailUsername.setText(model.getUsername());
        tvDetailWebsite.setText(model.getWebsite() == null || model.getWebsite().isEmpty() ? "—" : model.getWebsite());
        if (!tvDetailWebsite.getText().toString().equals("—")) {
            tvDetailWebsite.setPaintFlags(tvDetailWebsite.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);
        }
        tvDetailNote.setText(model.getNote() == null || model.getNote().isEmpty() ? "Aucune note" : model.getNote());
        tvDetailPassword.setText(isPasswordVisible ? realPassword : "••••••••••");
    }

    private void togglePassword() {
        isPasswordVisible = !isPasswordVisible;
        tvDetailPassword.setText(isPasswordVisible ? realPassword : "••••••••••");
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer cette entrée ?")
                .setMessage("Cette action est irréversible.")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    passwordRepository.deletePassword(passwordId);
                    Toast.makeText(this, "Entrée supprimée", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}
