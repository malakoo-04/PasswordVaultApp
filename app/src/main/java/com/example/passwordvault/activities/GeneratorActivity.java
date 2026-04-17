package com.example.passwordvault.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passwordvault.R;
import com.example.passwordvault.utils.PasswordGenerator;

public class GeneratorActivity extends AppCompatActivity {

    private ImageView btnBackGenerator;
    private TextView tvStrength, tvGeneratedPassword, tvLengthLabel;
    private View strengthFill;
    private Button btnRegenerate, btnCopyGenerated;
    private SeekBar seekPasswordLength;
    private Switch switchUppercase, switchLowercase, switchNumbers, switchSymbols;
    private boolean returnMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);

        returnMode = getIntent().getBooleanExtra("return_mode", false);

        btnBackGenerator = findViewById(R.id.btnBackGenerator);
        tvStrength = findViewById(R.id.tvStrength);
        tvGeneratedPassword = findViewById(R.id.tvGeneratedPassword);
        tvLengthLabel = findViewById(R.id.tvLengthLabel);
        strengthFill = findViewById(R.id.strengthFill);
        btnRegenerate = findViewById(R.id.btnRegenerate);
        btnCopyGenerated = findViewById(R.id.btnCopyGenerated);
        seekPasswordLength = findViewById(R.id.seekPasswordLength);
        switchUppercase = findViewById(R.id.switchUppercase);
        switchLowercase = findViewById(R.id.switchLowercase);
        switchNumbers = findViewById(R.id.switchNumbers);
        switchSymbols = findViewById(R.id.switchSymbols);

        if (returnMode) {
            btnCopyGenerated.setText("Utiliser");
        }

        btnBackGenerator.setOnClickListener(v -> finish());
        updateGeneratedPassword();

        seekPasswordLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateGeneratedPassword();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnRegenerate.setOnClickListener(v -> updateGeneratedPassword());

        btnCopyGenerated.setOnClickListener(v -> {
            String password = tvGeneratedPassword.getText().toString();
            if (password.startsWith("Choisissez")) {
                Toast.makeText(this, "Aucune configuration valide", Toast.LENGTH_SHORT).show();
                return;
            }

            if (returnMode) {
                Intent result = new Intent();
                result.putExtra("generated_password", password);
                setResult(RESULT_OK, result);
                finish();
            } else {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("generated_password", password);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Mot de passe copié", Toast.LENGTH_SHORT).show();
            }
        });

        switchUppercase.setOnCheckedChangeListener((buttonView, isChecked) -> updateGeneratedPassword());
        switchLowercase.setOnCheckedChangeListener((buttonView, isChecked) -> updateGeneratedPassword());
        switchNumbers.setOnCheckedChangeListener((buttonView, isChecked) -> updateGeneratedPassword());
        switchSymbols.setOnCheckedChangeListener((buttonView, isChecked) -> updateGeneratedPassword());
    }

    private void updateGeneratedPassword() {
        int length = seekPasswordLength.getProgress() + 8;
        tvLengthLabel.setText("Longueur: " + length);

        String password = PasswordGenerator.generate(
                length,
                switchUppercase.isChecked(),
                switchLowercase.isChecked(),
                switchNumbers.isChecked(),
                switchSymbols.isChecked()
        );

        boolean noOptionSelected = !switchUppercase.isChecked() && !switchLowercase.isChecked()
                && !switchNumbers.isChecked() && !switchSymbols.isChecked();

        if (noOptionSelected) {
            tvGeneratedPassword.setText("Choisissez au moins une option");
            tvStrength.setText("Force: Faible");
            updateStrengthBar(0.25f);
            return;
        }

        tvGeneratedPassword.setText(password);
        if (length >= 16) {
            tvStrength.setText("Force: Fort");
            updateStrengthBar(1f);
        } else if (length >= 12) {
            tvStrength.setText("Force: Moyen");
            updateStrengthBar(0.65f);
        } else {
            tvStrength.setText("Force: Faible");
            updateStrengthBar(0.35f);
        }
    }

    private void updateStrengthBar(float ratio) {
        strengthFill.post(() -> {
            int width = ((View) strengthFill.getParent()).getWidth();
            if (width > 0) {
                strengthFill.getLayoutParams().width = Math.max(1, (int) (width * ratio));
                strengthFill.requestLayout();
            }
        });
    }
}
