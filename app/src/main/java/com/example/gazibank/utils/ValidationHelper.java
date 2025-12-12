package com.example.gazibank.utils;

import android.widget.EditText;

public class ValidationHelper {

    // TC Kimlik numarası kontrolü
    public static boolean isValidTC(String tc) {
        if (tc == null || tc.length() != 11) {
            return false;
        }

        // Sadece rakam kontrolü
        try {
            Long.parseLong(tc);

            // İlk hane 0 olamaz
            if (tc.charAt(0) == '0') {
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Şifre kontrolü
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        return true;
    }

    // İsim kontrolü
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        // En az 2 karakter
        if (name.trim().length() < 2) {
            return false;
        }

        return true;
    }

    // Tutar kontrolü
    public static boolean isValidAmount(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return false;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            return amount > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // EditText boş mu kontrolü
    public static boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    // EditText'ten text al
    public static String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    // Tutarı formatla
    public static String formatAmount(double amount) {
        return String.format("%.2f TL", amount);
    }

    // Hata mesajı göster
    public static void showError(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
    }
}