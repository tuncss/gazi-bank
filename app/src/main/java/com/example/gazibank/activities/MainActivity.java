package com.example.gazibank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.example.gazibank.R;
import com.example.gazibank.database.DatabaseHelper;
import com.example.gazibank.models.Account;
import com.example.gazibank.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private TextView textViewUserName;
    private TextView textViewBalance;
    private TextView textViewAccountNumber;
    private Button buttonLogout;
    private CardView cardTransfer;
    private CardView cardHistory;
    private CardView cardFavorites;
    private CardView cardProfile;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize
        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        currentUserId = sessionManager.getUserId();

        // Giriş kontrolü
        if (!sessionManager.isLoggedIn()) {
            navigateToLogin();
            return;
        }

        // View'ları bağla
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewBalance = findViewById(R.id.textViewBalance);
        textViewAccountNumber = findViewById(R.id.textViewAccountNumber);
        buttonLogout = findViewById(R.id.buttonLogout);
        cardTransfer = findViewById(R.id.cardTransfer);
        cardHistory = findViewById(R.id.cardHistory);
        cardFavorites = findViewById(R.id.cardFavorites);
        cardProfile = findViewById(R.id.cardProfile);

        // Kullanıcı bilgilerini göster
        loadUserData();

        // Click listeners
        buttonLogout.setOnClickListener(v -> logout());
        cardTransfer.setOnClickListener(v -> navigateToTransfer());
        cardHistory.setOnClickListener(v -> navigateToHistory());
        cardFavorites.setOnClickListener(v -> navigateToFavorites());
        cardProfile.setOnClickListener(v -> navigateToProfile());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ekrana her dönüldüğünde bakiyeyi güncelle
        loadUserData();
    }

    private void loadUserData() {
        // Kullanıcı adını göster
        String userName = sessionManager.getUserName();
        textViewUserName.setText("Hoş geldiniz, " + userName);

        // Hesap bilgilerini getir
        Account account = databaseHelper.getAccountByUserId(currentUserId);

        if (account != null) {
            textViewBalance.setText(account.getFormattedBalance());
            textViewAccountNumber.setText("Hesap No: " + account.getAccountNumber());
        } else {
            Toast.makeText(this, "Hesap bilgileri yüklenemedi!", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        sessionManager.logoutUser();
        Toast.makeText(this, "Çıkış yapıldı", Toast.LENGTH_SHORT).show();
        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToTransfer() {
        Intent intent = new Intent(MainActivity.this, TransferActivity.class);
        startActivity(intent);
    }

    private void navigateToHistory() {
        Intent intent = new Intent(MainActivity.this, TransactionHistoryActivity.class);
        startActivity(intent);
    }

    private void navigateToFavorites() {
        Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
        startActivity(intent);
    }

    private void navigateToProfile() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Ana ekranda geri tuşuna basılınca uygulamadan çık
        super.onBackPressed();
        finishAffinity();
    }
}