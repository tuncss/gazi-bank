package com.example.gazibank.models;

public class Transaction {
    private int id;
    private int senderUserId;
    private int receiverUserId;
    private double amount;
    private String transactionDate;
    private String description;
    private String type; // "TRANSFER", "DEPOSIT", "WITHDRAW"

    // Constructor - Yeni işlem oluşturma
    public Transaction(int senderUserId, int receiverUserId, double amount, String type, String description) {
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    // Constructor - Veritabanından veri okuma
    public Transaction(int id, int senderUserId, int receiverUserId, double amount,
                       String transactionDate, String type, String description) {
        this.id = id;
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.type = type;
        this.description = description;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getSenderUserId() {
        return senderUserId;
    }

    public int getReceiverUserId() {
        return receiverUserId;
    }

    public double getAmount() {
        return amount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    // Tutarı formatla
    public String getFormattedAmount() {
        return String.format("%.2f TL", amount);
    }

    // İşlem tipine göre renk belirleme (UI için)
    public boolean isIncoming(int currentUserId) {
        return receiverUserId == currentUserId;
    }

    public boolean isOutgoing(int currentUserId) {
        return senderUserId == currentUserId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", senderUserId=" + senderUserId +
                ", receiverUserId=" + receiverUserId +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", date='" + transactionDate + '\'' +
                '}';
    }
}