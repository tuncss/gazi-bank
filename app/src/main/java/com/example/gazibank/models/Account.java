package com.example.gazibank.models;

public class Account {
    private int id;
    private int userId;
    private double balance;
    private String accountNumber;
    private String createdAt;

    // Constructor - Yeni hesap oluşturma
    public Account(int userId, double balance, String accountNumber) {
        this.userId = userId;
        this.balance = balance;
        this.accountNumber = accountNumber;
    }

    // Constructor - Veritabanından veri okuma
    public Account(int id, int userId, double balance, String accountNumber, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Para çekme
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    // Para yatırma
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    // Bakiye kontrolü
    public boolean hasSufficientBalance(double amount) {
        return balance >= amount;
    }

    // Bakiyeyi formatla (Türk Lirası)
    public String getFormattedBalance() {
        return String.format("%.2f TL", balance);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", balance=" + balance +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }
}