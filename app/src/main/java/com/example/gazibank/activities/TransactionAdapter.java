package com.example.gazibank.activities;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gazibank.R;
import com.example.gazibank.database.DatabaseHelper;
import com.example.gazibank.models.Transaction;
import com.example.gazibank.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactions;
    private int currentUserId;
    private DatabaseHelper databaseHelper;

    public TransactionAdapter(List<Transaction> transactions, int currentUserId, DatabaseHelper databaseHelper) {
        this.transactions = transactions;
        this.currentUserId = currentUserId;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        boolean isIncoming = transaction.isIncoming(currentUserId);
        boolean isOutgoing = transaction.isOutgoing(currentUserId);

        // İşlem yönüne göre bilgileri ayarla
        if (isIncoming) {
            // Gelen para
            User sender = databaseHelper.getUserById(transaction.getSenderUserId());
            holder.textViewIcon.setText("💰");
            holder.textViewTitle.setText("Para Aldınız");
            holder.textViewDescription.setText(sender != null ?
                    sender.getFullName() + " - " + transaction.getDescription() :
                    transaction.getDescription());
            holder.textViewAmount.setText("+ " + transaction.getFormattedAmount());
            holder.textViewAmount.setTextColor(Color.parseColor("#4CAF50")); // Yeşil
        } else if (isOutgoing) {
            // Giden para
            User receiver = databaseHelper.getUserById(transaction.getReceiverUserId());
            holder.textViewIcon.setText("💸");
            holder.textViewTitle.setText("Para Gönderdiniz");
            holder.textViewDescription.setText(receiver != null ?
                    receiver.getFullName() + " - " + transaction.getDescription() :
                    transaction.getDescription());
            holder.textViewAmount.setText("- " + transaction.getFormattedAmount());
            holder.textViewAmount.setTextColor(Color.parseColor("#F44336")); // Kırmızı
        }

        // Tarihi formatla
        holder.textViewDate.setText(formatDate(transaction.getTransactionDate()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateString;
        }
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewIcon;
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewDate;
        TextView textViewAmount;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewIcon = itemView.findViewById(R.id.textViewIcon);
            textViewTitle = itemView.findViewById(R.id.textViewTransactionTitle);
            textViewDescription = itemView.findViewById(R.id.textViewTransactionDescription);
            textViewDate = itemView.findViewById(R.id.textViewTransactionDate);
            textViewAmount = itemView.findViewById(R.id.textViewTransactionAmount);
        }
    }
}