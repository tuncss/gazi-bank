package com.example.gazibank.models;

public class Favorite {
    private int id;
    private int userId;
    private int favoriteUserId;
    private String favoriteUserName;
    private String favoriteUserTC;
    private String addedAt;

    // Constructor - Yeni favori ekleme
    public Favorite(int userId, int favoriteUserId) {
        this.userId = userId;
        this.favoriteUserId = favoriteUserId;
    }

    // Constructor - Veritabanından veri okuma (join ile)
    public Favorite(int id, int userId, int favoriteUserId, String favoriteUserName,
                    String favoriteUserTC, String addedAt) {
        this.id = id;
        this.userId = userId;
        this.favoriteUserId = favoriteUserId;
        this.favoriteUserName = favoriteUserName;
        this.favoriteUserTC = favoriteUserTC;
        this.addedAt = addedAt;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getFavoriteUserId() {
        return favoriteUserId;
    }

    public String getFavoriteUserName() {
        return favoriteUserName;
    }

    public String getFavoriteUserTC() {
        return favoriteUserTC;
    }

    public String getAddedAt() {
        return addedAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setFavoriteUserName(String favoriteUserName) {
        this.favoriteUserName = favoriteUserName;
    }

    public void setFavoriteUserTC(String favoriteUserTC) {
        this.favoriteUserTC = favoriteUserTC;
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "id=" + id +
                ", userId=" + userId +
                ", favoriteUserId=" + favoriteUserId +
                ", favoriteUserName='" + favoriteUserName + '\'' +
                '}';
    }
}