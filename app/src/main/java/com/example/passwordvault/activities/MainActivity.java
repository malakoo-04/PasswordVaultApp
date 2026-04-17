package com.example.passwordvault.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passwordvault.R;
import com.example.passwordvault.models.PasswordModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.passwordvault.repository.PasswordRepository;
import com.example.passwordvault.views.PasswordAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvCount;
    private EditText etSearch;
    private ImageView btnSettings;
    private Button btnAddFirst;
    private ImageButton fabAdd;
    private LinearLayout emptyStateLayout;
    private RecyclerView recyclerPasswords;

    private FloatingActionButton fabGenerator;
    private PasswordRepository passwordRepository;
    private PasswordAdapter passwordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordRepository = new PasswordRepository(this);

        tvCount = findViewById(R.id.tvCount);
        etSearch = findViewById(R.id.etSearch);
        btnSettings = findViewById(R.id.btnSettings);
        btnAddFirst = findViewById(R.id.btnAddFirst);
        fabAdd = findViewById(R.id.fabAdd);
        fabGenerator = findViewById(R.id.fabGenerator);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        recyclerPasswords = findViewById(R.id.recyclerPasswords);

        recyclerPasswords.setLayoutManager(new LinearLayoutManager(this));
        passwordAdapter = new PasswordAdapter(this::openDetails);
        recyclerPasswords.setAdapter(passwordAdapter);

        fabAdd.setOnClickListener(v -> openAddEditScreen(-1));
        fabGenerator.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GeneratorActivity.class);
            startActivity(intent);
        });
        btnAddFirst.setOnClickListener(v -> openAddEditScreen(-1));
        btnSettings.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SettingsActivity.class)));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { loadPasswords(s.toString()); }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPasswords(etSearch.getText().toString());
    }

    private void loadPasswords(String query) {
        ArrayList<PasswordModel> passwords = passwordRepository.searchPasswords(query);
        passwordAdapter.submitList(passwords);
        updateUI(passwords.size());
    }

    private void updateUI(int size) {
        if (size == 0) {
            emptyStateLayout.setVisibility(View.VISIBLE);
            recyclerPasswords.setVisibility(View.GONE);
        } else {
            emptyStateLayout.setVisibility(View.GONE);
            recyclerPasswords.setVisibility(View.VISIBLE);
        }
        tvCount.setText(size + " compte(s) enregistré(s)");
    }

    private void openAddEditScreen(int id) {
        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
        if (id != -1) {
            intent.putExtra("password_id", id);
        }
        startActivity(intent);
    }

    private void openDetails(PasswordModel passwordModel) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("password_id", passwordModel.getId());
        startActivity(intent);
    }
}
