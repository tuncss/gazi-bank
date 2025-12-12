package com.example.gazibank.models;

public class User {
    private int id;
    private String tc;
    private String password;
    private String firstName;
    private String lastName;
    private String createdAt;

    // Constructor - Yeni kullanıcı oluşturma
    public User(String tc, String password, String firstName, String lastName) {
        this.tc = tc;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Constructor - Veritabanından veri okuma
    public User(int id, String tc, String password, String firstName, String lastName, String createdAt) {
        this.id = id;
        this.tc = tc;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTc() {
        return tc;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // TC numarası validasyonu
    public static boolean isValidTC(String tc) {
        if (tc == null || tc.length() != 11) {
            return false;
        }

        // Sadece rakam kontrolü
        try {
            Long.parseLong(tc);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", tc='" + tc + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}