package com.example.gazibank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.gazibank.R;
import com.example.gazibank.database.DatabaseHelper;
import com.example.gazibank.models.User;
import com.example.gazibank.utils.SessionManager;
import com.example.gazibank.utils.ValidationHelper;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editTextTC;
    private TextInputEditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize
        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Eğer zaten giriş yapılmışsa direkt ana ekrana git
        if (sessionManager.isLoggedIn()) {
            navigateToMain();
            return;
        }

        // View'ları bağla
        editTextTC = findViewById(R.id.editTextTC);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        // Giriş butonu click listener
        buttonLogin.setOnClickListener(v -> loginUser());
        textViewRegister.setOnClickListener(v -> navigateToRegister());
    }

    private void loginUser() {
        String tc = ValidationHelper.getText(editTextTC);
        String password = ValidationHelper.getText(editTextPassword);

        // Validasyonlar
        if (!ValidationHelper.isValidTC(tc)) {
            ValidationHelper.showError(editTextTC, "Geçerli bir TC Kimlik No giriniz (11 hane)");
            return;
        }

        if (!ValidationHelper.isValidPassword(password)) {
            ValidationHelper.showError(editTextPassword, "Şifre en az 6 karakter olmalıdır");
            return;
        }

        // Veritabanından kullanıcıyı kontrol et
        User user = databaseHelper.loginUser(tc, password);

        if (user != null) {
            // Başarılı giriş
            sessionManager.createLoginSession(user.getId(), user.getTc(), user.getFullName());
            Toast.makeText(this, "Hoş geldiniz, " + user.getFirstName(), Toast.LENGTH_SHORT).show();
            navigateToMain();
        } else {
            // Başarısız giriş
            Toast.makeText(this, "TC Kimlik No veya şifre hatalı!", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Geri tuşunu devre dışı bırak (uygulamadan çıkılmasın)
        super.onBackPressed();
        finishAffinity();
    }
}