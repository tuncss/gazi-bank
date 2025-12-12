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
import com.example.gazibank.utils.ValidationHelper;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText editTextFirstName;
    private TextInputEditText editTextLastName;
    private TextInputEditText editTextTC;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextPasswordConfirm;
    private TextInputEditText editTextInitialBalance;
    private Button buttonRegister;
    private TextView textViewLogin;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize
        databaseHelper = new DatabaseHelper(this);

        // View'ları bağla
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextTC = findViewById(R.id.editTextTC);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
        editTextInitialBalance = findViewById(R.id.editTextInitialBalance);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);

        // Click listeners
        buttonRegister.setOnClickListener(v -> registerUser());
        textViewLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void registerUser() {
        String firstName = ValidationHelper.getText(editTextFirstName);
        String lastName = ValidationHelper.getText(editTextLastName);
        String tc = ValidationHelper.getText(editTextTC);
        String password = ValidationHelper.getText(editTextPassword);
        String passwordConfirm = ValidationHelper.getText(editTextPasswordConfirm);
        String balanceStr = ValidationHelper.getText(editTextInitialBalance);

        // Validasyonlar
        if (!ValidationHelper.isValidName(firstName)) {
            ValidationHelper.showError(editTextFirstName, "Geçerli bir ad giriniz");
            return;
        }

        if (!ValidationHelper.isValidName(lastName)) {
            ValidationHelper.showError(editTextLastName, "Geçerli bir soyad giriniz");
            return;
        }

        if (!ValidationHelper.isValidTC(tc)) {
            ValidationHelper.showError(editTextTC, "Geçerli bir TC Kimlik No giriniz (11 hane)");
            return;
        }

        // TC'nin daha önce kullanılıp kullanılmadığını kontrol et
        User existingUser = databaseHelper.getUserByTC(tc);
        if (existingUser != null) {
            ValidationHelper.showError(editTextTC, "Bu TC Kimlik No ile zaten bir hesap var!");
            return;
        }

        if (!ValidationHelper.isValidPassword(password)) {
            ValidationHelper.showError(editTextPassword, "Şifre en az 6 karakter olmalıdır");
            return;
        }

        if (!password.equals(passwordConfirm)) {
            ValidationHelper.showError(editTextPasswordConfirm, "Şifreler eşleşmiyor!");
            return;
        }

        if (!ValidationHelper.isValidAmount(balanceStr)) {
            ValidationHelper.showError(editTextInitialBalance, "Geçerli bir tutar giriniz");
            return;
        }

        double initialBalance = Double.parseDouble(balanceStr);
        if (initialBalance < 0) {
            ValidationHelper.showError(editTextInitialBalance, "Bakiye 0'dan küçük olamaz");
            return;
        }

        // Kullanıcıyı kaydet
        boolean success = databaseHelper.registerUser(firstName, lastName, tc, password, initialBalance);

        if (success) {
            Toast.makeText(this, "Hesabınız başarıyla oluşturuldu! Giriş yapabilirsiniz.", Toast.LENGTH_LONG).show();
            navigateToLogin();
        } else {
            Toast.makeText(this, "Kayıt başarısız! Lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateToLogin();
    }
}