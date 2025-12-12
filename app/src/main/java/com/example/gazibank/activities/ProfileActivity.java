package com.example.gazibank.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gazibank.R;
import com.example.gazibank.database.DatabaseHelper;
import com.example.gazibank.models.Account;
import com.example.gazibank.models.User;
import com.example.gazibank.utils.SessionManager;
import com.example.gazibank.utils.ValidationHelper;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewProfileTC;
    private TextView textViewProfileName;
    private TextView textViewProfileAccount;
    private TextInputEditText editTextFirstName;
    private TextInputEditText editTextLastName;
    private TextInputEditText editTextCurrentPassword;
    private TextInputEditText editTextNewPassword;
    private TextInputEditText editTextNewPasswordConfirm;
    private Button buttonBack;
    private Button buttonUpdateProfile;
    private Button buttonChangePassword;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private int currentUserId;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize
        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        currentUserId = sessionManager.getUserId();

        // View'ları bağla
        textViewProfileTC = findViewById(R.id.textViewProfileTC);
        textViewProfileName = findViewById(R.id.textViewProfileName);
        textViewProfileAccount = findViewById(R.id.textViewProfileAccount);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextCurrentPassword = findViewById(R.id.editTextCurrentPassword);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextNewPasswordConfirm = findViewById(R.id.editTextNewPasswordConfirm);
        buttonBack = findViewById(R.id.buttonBack);
        buttonUpdateProfile = findViewById(R.id.buttonUpdateProfile);
        buttonChangePassword = findViewById(R.id.buttonChangePassword);

        // Kullanıcı bilgilerini yükle
        loadUserData();

        // Click listeners
        buttonBack.setOnClickListener(v -> finish());
        buttonUpdateProfile.setOnClickListener(v -> updateProfile());
        buttonChangePassword.setOnClickListener(v -> changePassword());
    }

    private void loadUserData() {
        currentUser = databaseHelper.getUserById(currentUserId);

        if (currentUser != null) {
            textViewProfileTC.setText(currentUser.getTc());
            textViewProfileName.setText(currentUser.getFullName());
            editTextFirstName.setText(currentUser.getFirstName());
            editTextLastName.setText(currentUser.getLastName());

            // Hesap numarasını al
            Account account = databaseHelper.getAccountByUserId(currentUserId);
            if (account != null) {
                textViewProfileAccount.setText(account.getAccountNumber());
            }
        }
    }

    private void updateProfile() {
        String firstName = ValidationHelper.getText(editTextFirstName);
        String lastName = ValidationHelper.getText(editTextLastName);

        // Validasyonlar
        if (!ValidationHelper.isValidName(firstName)) {
            ValidationHelper.showError(editTextFirstName, "Geçerli bir ad giriniz");
            return;
        }

        if (!ValidationHelper.isValidName(lastName)) {
            ValidationHelper.showError(editTextLastName, "Geçerli bir soyad giriniz");
            return;
        }

        // Bilgileri güncelle
        boolean success = databaseHelper.updateUser(currentUserId, firstName, lastName);

        if (success) {
            // Session'daki adı güncelle
            sessionManager.updateUserName(firstName + " " + lastName);

            // Ekrandaki bilgileri güncelle
            textViewProfileName.setText(firstName + " " + lastName);

            Toast.makeText(this, "Bilgileriniz güncellendi", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Güncelleme başarısız!", Toast.LENGTH_SHORT).show();
        }
    }

    private void changePassword() {
        String currentPassword = ValidationHelper.getText(editTextCurrentPassword);
        String newPassword = ValidationHelper.getText(editTextNewPassword);
        String newPasswordConfirm = ValidationHelper.getText(editTextNewPasswordConfirm);

        // Validasyonlar
        if (ValidationHelper.isEmpty(editTextCurrentPassword)) {
            ValidationHelper.showError(editTextCurrentPassword, "Mevcut şifrenizi giriniz");
            return;
        }

        // Mevcut şifre kontrolü
        if (!currentPassword.equals(currentUser.getPassword())) {
            ValidationHelper.showError(editTextCurrentPassword, "Mevcut şifre hatalı!");
            return;
        }

        if (!ValidationHelper.isValidPassword(newPassword)) {
            ValidationHelper.showError(editTextNewPassword, "Yeni şifre en az 6 karakter olmalıdır");
            return;
        }

        // Yeni şifre eşleşme kontrolü
        if (!newPassword.equals(newPasswordConfirm)) {
            ValidationHelper.showError(editTextNewPasswordConfirm, "Şifreler eşleşmiyor!");
            return;
        }

        // Eski ve yeni şifre aynı olmamalı
        if (currentPassword.equals(newPassword)) {
            ValidationHelper.showError(editTextNewPassword, "Yeni şifre eskisiyle aynı olamaz!");
            return;
        }

        // Şifreyi güncelle
        boolean success = databaseHelper.updatePassword(currentUserId, newPassword);

        if (success) {
            Toast.makeText(this, "Şifreniz başarıyla değiştirildi", Toast.LENGTH_LONG).show();

            // Şifre alanlarını temizle
            editTextCurrentPassword.setText("");
            editTextNewPassword.setText("");
            editTextNewPasswordConfirm.setText("");
        } else {
            Toast.makeText(this, "Şifre değiştirme başarısız!", Toast.LENGTH_SHORT).show();
        }
    }
}