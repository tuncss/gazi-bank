package com.example.gazibank.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gazibank.R;
import com.example.gazibank.database.DatabaseHelper;
import com.example.gazibank.models.Transaction;
import com.example.gazibank.utils.SessionManager;

import java.util.List;

public class TransactionHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTransactions;
    private LinearLayout layoutEmptyState;
    private Button buttonBack;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private int currentUserId;
    private TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        // Initialize
        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        currentUserId = sessionManager.getUserId();

        // View'ları bağla
        recyclerViewTransactions = findViewById(R.id.recyclerViewTransactions);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        buttonBack = findViewById(R.id.buttonBack);

        // RecyclerView ayarları
        recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(this));

        // Click listeners
        buttonBack.setOnClickListener(v -> finish());

        // İşlemleri yükle
        loadTransactions();
    }

    private void loadTransactions() {
        // Son 20 işlemi getir
        List<Transaction> transactions = databaseHelper.getTransactionHistory(currentUserId, 20);

        if (transactions.isEmpty()) {
            // Hiç işlem yoksa boş durum göster
            recyclerViewTransactions.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            // İşlemleri listele
            recyclerViewTransactions.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);

            adapter = new TransactionAdapter(transactions, currentUserId, databaseHelper);
            recyclerViewTransactions.setAdapter(adapter);
        }
    }
}