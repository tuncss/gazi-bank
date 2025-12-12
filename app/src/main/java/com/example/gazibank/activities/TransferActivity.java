package com.example.gazibank.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gazibank.R;
import com.example.gazibank.database.DatabaseHelper;
import com.example.gazibank.models.Account;
import com.example.gazibank.models.User;
import com.example.gazibank.utils.SessionManager;
import com.example.gazibank.utils.ValidationHelper;
import com.google.android.material.textfield.TextInputEditText;

public class TransferActivity extends AppCompatActivity {

    private TextView textViewCurrentBalance;
    private TextView textViewReceiverName;
    private TextInputEditText editTextReceiverTC;
    private TextInputEditText editTextAmount;
    private TextInputEditText editTextDescription;
    private Button buttonBack;
    private Button buttonCheckReceiver;
    private Button buttonTransfer;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private int currentUserId;
    private Account currentAccount;
    private User receiverUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        // Initialize
        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        currentUserId = sessionManager.getUserId();

        // View'ları bağla
        textViewCurrentBalance = findViewById(R.id.textViewCurrentBalance);
        textViewReceiverName = findViewById(R.id.textViewReceiverName);
        editTextReceiverTC = findViewById(R.id.editTextReceiverTC);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonBack = findViewById(R.id.buttonBack);
        buttonCheckReceiver = findViewById(R.id.buttonCheckReceiver);
        buttonTransfer = findViewById(R.id.buttonTransfer);

        // Mevcut bakiyeyi göster
        loadCurrentBalance();

        // TC numarası değiştiğinde alıcı bilgisini sıfırla
        editTextReceiverTC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewReceiverName.setVisibility(View.GONE);
                buttonTransfer.setEnabled(false);
                receiverUser = null;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Click listeners
        buttonBack.setOnClickListener(v -> finish());
        buttonCheckReceiver.setOnClickListener(v -> checkReceiver());
        buttonTransfer.setOnClickListener(v -> showTransferConfirmation());
    }

    private void loadCurrentBalance() {
        currentAccount = databaseHelper.getAccountByUserId(currentUserId);
        if (currentAccount != null) {
            textViewCurrentBalance.setText(currentAccount.getFormattedBalance());
        }
    }

    private void checkReceiver() {
        String receiverTC = ValidationHelper.getText(editTextReceiverTC);

        // TC validasyonu
        if (!ValidationHelper.isValidTC(receiverTC)) {
            ValidationHelper.showError(editTextReceiverTC, "Geçerli bir TC Kimlik No giriniz");
            return;
        }

        // Kendi hesabına transfer kontrolü
        if (receiverTC.equals(sessionManager.getUserTC())) {
            ValidationHelper.showError(editTextReceiverTC, "Kendi hesabınıza transfer yapamazsınız");
            return;
        }

        // Alıcıyı veritabanında ara
        receiverUser = databaseHelper.getUserByTC(receiverTC);

        if (receiverUser != null) {
            // Alıcı bulundu
            textViewReceiverName.setText("✓ Alıcı: " + receiverUser.getFullName());
            textViewReceiverName.setVisibility(View.VISIBLE);
            buttonTransfer.setEnabled(true);
            Toast.makeText(this, "Alıcı doğrulandı", Toast.LENGTH_SHORT).show();
        } else {
            // Alıcı bulunamadı
            textViewReceiverName.setVisibility(View.GONE);
            buttonTransfer.setEnabled(false);
            ValidationHelper.showError(editTextReceiverTC, "Bu TC'ye kayıtlı kullanıcı bulunamadı");
        }
    }

    private void showTransferConfirmation() {
        String amountStr = ValidationHelper.getText(editTextAmount);
        String description = ValidationHelper.getText(editTextDescription);

        // Tutar validasyonu
        if (!ValidationHelper.isValidAmount(amountStr)) {
            ValidationHelper.showError(editTextAmount, "Geçerli bir tutar giriniz");
            return;
        }

        double amount = Double.parseDouble(amountStr);

        // Bakiye kontrolü
        if (!currentAccount.hasSufficientBalance(amount)) {
            ValidationHelper.showError(editTextAmount, "Yetersiz bakiye!");
            return;
        }

        // Onay dialogu göster
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Transfer Onayı");
        builder.setMessage(
                "Alıcı: " + receiverUser.getFullName() + "\n" +
                        "Tutar: " + ValidationHelper.formatAmount(amount) + "\n" +
                        "Açıklama: " + (description.isEmpty() ? "-" : description) + "\n\n" +
                        "Transfer işlemini onaylıyor musunuz?"
        );

        builder.setPositiveButton("Onayla", (dialog, which) -> performTransfer());
        builder.setNegativeButton("İptal", null);
        builder.show();
    }

    private void performTransfer() {
        String amountStr = ValidationHelper.getText(editTextAmount);
        String description = ValidationHelper.getText(editTextDescription);
        double amount = Double.parseDouble(amountStr);

        if (description.isEmpty()) {
            description = "Para transferi";
        }

        // Transfer işlemini gerçekleştir
        boolean success = databaseHelper.transferMoney(
                currentUserId,
                receiverUser.getId(),
                amount,
                description
        );

        if (success) {
            Toast.makeText(this, "Transfer başarılı!", Toast.LENGTH_LONG).show();

            // Favori ekleme önerisi
            if (!databaseHelper.isFavorite(currentUserId, receiverUser.getId())) {
                showAddFavoriteDialog();
            } else {
                finish();
            }
        } else {
            Toast.makeText(this, "Transfer başarısız! Lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddFavoriteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Favorilere Ekle");
        builder.setMessage(receiverUser.getFullName() + " kişisini favorilerinize eklemek ister misiniz?");

        builder.setPositiveButton("Evet", (dialog, which) -> {
            boolean added = databaseHelper.addFavorite(currentUserId, receiverUser.getId());
            if (added) {
                Toast.makeText(this, "Favorilere eklendi", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        builder.setNegativeButton("Hayır", (dialog, which) -> finish());
        builder.show();
    }
}