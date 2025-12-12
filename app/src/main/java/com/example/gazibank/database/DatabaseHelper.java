package com.example.gazibank.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gazibank.models.Account;
import com.example.gazibank.models.Favorite;
import com.example.gazibank.models.Transaction;
import com.example.gazibank.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GaziBank.db";
    private static final int DATABASE_VERSION = 1;

    // Tablo isimleri
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ACCOUNTS = "accounts";
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String TABLE_FAVORITES = "favorites";

    // Users tablosu sütunları
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_TC = "tc";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_FIRST_NAME = "first_name";
    private static final String COLUMN_USER_LAST_NAME = "last_name";
    private static final String COLUMN_USER_CREATED_AT = "created_at";

    // Accounts tablosu sütunları
    private static final String COLUMN_ACCOUNT_ID = "id";
    private static final String COLUMN_ACCOUNT_USER_ID = "user_id";
    private static final String COLUMN_ACCOUNT_BALANCE = "balance";
    private static final String COLUMN_ACCOUNT_NUMBER = "account_number";
    private static final String COLUMN_ACCOUNT_CREATED_AT = "created_at";

    // Transactions tablosu sütunları
    private static final String COLUMN_TRANSACTION_ID = "id";
    private static final String COLUMN_TRANSACTION_SENDER_ID = "sender_user_id";
    private static final String COLUMN_TRANSACTION_RECEIVER_ID = "receiver_user_id";
    private static final String COLUMN_TRANSACTION_AMOUNT = "amount";
    private static final String COLUMN_TRANSACTION_DATE = "transaction_date";
    private static final String COLUMN_TRANSACTION_TYPE = "type";
    private static final String COLUMN_TRANSACTION_DESCRIPTION = "description";

    // Favorites tablosu sütunları
    private static final String COLUMN_FAVORITE_ID = "id";
    private static final String COLUMN_FAVORITE_USER_ID = "user_id";
    private static final String COLUMN_FAVORITE_FAVORITE_USER_ID = "favorite_user_id";
    private static final String COLUMN_FAVORITE_ADDED_AT = "added_at";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users tablosu oluştur
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_TC + " TEXT UNIQUE NOT NULL,"
                + COLUMN_USER_PASSWORD + " TEXT NOT NULL,"
                + COLUMN_USER_FIRST_NAME + " TEXT NOT NULL,"
                + COLUMN_USER_LAST_NAME + " TEXT NOT NULL,"
                + COLUMN_USER_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Accounts tablosu oluştur
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + "("
                + COLUMN_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ACCOUNT_USER_ID + " INTEGER NOT NULL,"
                + COLUMN_ACCOUNT_BALANCE + " REAL DEFAULT 0,"
                + COLUMN_ACCOUNT_NUMBER + " TEXT UNIQUE NOT NULL,"
                + COLUMN_ACCOUNT_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(" + COLUMN_ACCOUNT_USER_ID + ") REFERENCES "
                + TABLE_USERS + "(" + COLUMN_USER_ID + ")"
                + ")";
        db.execSQL(CREATE_ACCOUNTS_TABLE);

        // Transactions tablosu oluştur
        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                + COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TRANSACTION_SENDER_ID + " INTEGER NOT NULL,"
                + COLUMN_TRANSACTION_RECEIVER_ID + " INTEGER NOT NULL,"
                + COLUMN_TRANSACTION_AMOUNT + " REAL NOT NULL,"
                + COLUMN_TRANSACTION_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + COLUMN_TRANSACTION_TYPE + " TEXT NOT NULL,"
                + COLUMN_TRANSACTION_DESCRIPTION + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_TRANSACTION_SENDER_ID + ") REFERENCES "
                + TABLE_USERS + "(" + COLUMN_USER_ID + "),"
                + "FOREIGN KEY(" + COLUMN_TRANSACTION_RECEIVER_ID + ") REFERENCES "
                + TABLE_USERS + "(" + COLUMN_USER_ID + ")"
                + ")";
        db.execSQL(CREATE_TRANSACTIONS_TABLE);

        // Favorites tablosu oluştur
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FAVORITE_USER_ID + " INTEGER NOT NULL,"
                + COLUMN_FAVORITE_FAVORITE_USER_ID + " INTEGER NOT NULL,"
                + COLUMN_FAVORITE_ADDED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(" + COLUMN_FAVORITE_USER_ID + ") REFERENCES "
                + TABLE_USERS + "(" + COLUMN_USER_ID + "),"
                + "FOREIGN KEY(" + COLUMN_FAVORITE_FAVORITE_USER_ID + ") REFERENCES "
                + TABLE_USERS + "(" + COLUMN_USER_ID + "),"
                + "UNIQUE(" + COLUMN_FAVORITE_USER_ID + "," + COLUMN_FAVORITE_FAVORITE_USER_ID + ")"
                + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);

        // Test verileri ekle
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Test verileri ekleme
    private void insertSampleData(SQLiteDatabase db) {
        // 3 test kullanıcısı
        long userId1 = insertUser(db, "12345678901", "123456", "Ahmet", "Yılmaz");
        long userId2 = insertUser(db, "98765432109", "123456", "Ayşe", "Kara");
        long userId3 = insertUser(db, "11111111111", "123456", "Mehmet", "Demir");

        // Her kullanıcıya hesap
        insertAccount(db, userId1, 5000.0, "TR" + userId1 + "001");
        insertAccount(db, userId2, 3000.0, "TR" + userId2 + "002");
        insertAccount(db, userId3, 10000.0, "TR" + userId3 + "003");
    }

    private long insertUser(SQLiteDatabase db, String tc, String password,
                            String firstName, String lastName) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_TC, tc);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_FIRST_NAME, firstName);
        values.put(COLUMN_USER_LAST_NAME, lastName);
        return db.insert(TABLE_USERS, null, values);
    }

    private long insertAccount(SQLiteDatabase db, long userId, double balance,
                               String accountNumber) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACCOUNT_USER_ID, userId);
        values.put(COLUMN_ACCOUNT_BALANCE, balance);
        values.put(COLUMN_ACCOUNT_NUMBER, accountNumber);
        return db.insert(TABLE_ACCOUNTS, null, values);
    }

    // ===== USER İŞLEMLERİ =====

    // Yeni kullanıcı kaydı
    public boolean registerUser(String firstName, String lastName, String tc, String password, double initialBalance) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            // Kullanıcıyı ekle
            ContentValues userValues = new ContentValues();
            userValues.put(COLUMN_USER_TC, tc);
            userValues.put(COLUMN_USER_PASSWORD, password);
            userValues.put(COLUMN_USER_FIRST_NAME, firstName);
            userValues.put(COLUMN_USER_LAST_NAME, lastName);

            long userId = db.insert(TABLE_USERS, null, userValues);

            if (userId == -1) {
                return false;
            }

            // Hesap oluştur
            String accountNumber = "TR" + userId + String.format("%03d", (int)(Math.random() * 1000));
            ContentValues accountValues = new ContentValues();
            accountValues.put(COLUMN_ACCOUNT_USER_ID, userId);
            accountValues.put(COLUMN_ACCOUNT_BALANCE, initialBalance);
            accountValues.put(COLUMN_ACCOUNT_NUMBER, accountNumber);

            long accountId = db.insert(TABLE_ACCOUNTS, null, accountValues);

            if (accountId != -1) {
                db.setTransactionSuccessful();
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }

    // Kullanıcı girişi kontrolü
    public User loginUser(String tc, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        String query = "SELECT * FROM " + TABLE_USERS
                + " WHERE " + COLUMN_USER_TC + " = ? AND "
                + COLUMN_USER_PASSWORD + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{tc, password});

        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
        }
        cursor.close();
        return user;
    }

    // TC ile kullanıcı getir
    public User getUserByTC(String tc) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        Cursor cursor = db.query(TABLE_USERS, null,
                COLUMN_USER_TC + " = ?", new String[]{tc},
                null, null, null);

        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
        }
        cursor.close();
        return user;
    }

    // ID ile kullanıcı getir
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        Cursor cursor = db.query(TABLE_USERS, null,
                COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
        }
        cursor.close();
        return user;
    }

    // Şifre güncelle
    public boolean updatePassword(int userId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PASSWORD, newPassword);

        int rows = db.update(TABLE_USERS, values,
                COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});
        return rows > 0;
    }

    // Kullanıcı bilgilerini güncelle
    public boolean updateUser(int userId, String firstName, String lastName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FIRST_NAME, firstName);
        values.put(COLUMN_USER_LAST_NAME, lastName);

        int rows = db.update(TABLE_USERS, values,
                COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});
        return rows > 0;
    }

    // ===== ACCOUNT İŞLEMLERİ =====

    // Kullanıcının hesabını getir
    public Account getAccountByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Account account = null;

        Cursor cursor = db.query(TABLE_ACCOUNTS, null,
                COLUMN_ACCOUNT_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            account = new Account(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
        }
        cursor.close();
        return account;
    }

    // Bakiye güncelle
    public boolean updateBalance(int userId, double newBalance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACCOUNT_BALANCE, newBalance);

        int rows = db.update(TABLE_ACCOUNTS, values,
                COLUMN_ACCOUNT_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});
        return rows > 0;
    }

    // ===== TRANSACTION İŞLEMLERİ =====

    // Para transferi yap
    public boolean transferMoney(int senderId, int receiverId, double amount, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            // Gönderenin bakiyesini azalt
            Account senderAccount = getAccountByUserId(senderId);
            if (senderAccount == null || !senderAccount.hasSufficientBalance(amount)) {
                return false;
            }

            // Alıcının hesabını kontrol et
            Account receiverAccount = getAccountByUserId(receiverId);
            if (receiverAccount == null) {
                return false;
            }

            // Bakiyeleri güncelle
            double newSenderBalance = senderAccount.getBalance() - amount;
            double newReceiverBalance = receiverAccount.getBalance() + amount;

            updateBalance(senderId, newSenderBalance);
            updateBalance(receiverId, newReceiverBalance);

            // İşlemi kaydet
            ContentValues values = new ContentValues();
            values.put(COLUMN_TRANSACTION_SENDER_ID, senderId);
            values.put(COLUMN_TRANSACTION_RECEIVER_ID, receiverId);
            values.put(COLUMN_TRANSACTION_AMOUNT, amount);
            values.put(COLUMN_TRANSACTION_TYPE, "TRANSFER");
            values.put(COLUMN_TRANSACTION_DESCRIPTION, description);

            long result = db.insert(TABLE_TRANSACTIONS, null, values);

            if (result != -1) {
                db.setTransactionSuccessful();
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }

    // Kullanıcının işlem geçmişini getir
    public List<Transaction> getTransactionHistory(int userId, int limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Transaction> transactions = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_TRANSACTIONS
                + " WHERE " + COLUMN_TRANSACTION_SENDER_ID + " = ? OR "
                + COLUMN_TRANSACTION_RECEIVER_ID + " = ?"
                + " ORDER BY " + COLUMN_TRANSACTION_DATE + " DESC"
                + " LIMIT ?";

        Cursor cursor = db.rawQuery(query, new String[]{
                String.valueOf(userId),
                String.valueOf(userId),
                String.valueOf(limit)
        });

        while (cursor.moveToNext()) {
            Transaction transaction = new Transaction(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getDouble(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)
            );
            transactions.add(transaction);
        }
        cursor.close();
        return transactions;
    }

    // ===== FAVORITE İŞLEMLERİ =====

    // Favori ekle
    public boolean addFavorite(int userId, int favoriteUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FAVORITE_USER_ID, userId);
        values.put(COLUMN_FAVORITE_FAVORITE_USER_ID, favoriteUserId);

        long result = db.insert(TABLE_FAVORITES, null, values);
        return result != -1;
    }

    // Favori sil
    public boolean removeFavorite(int userId, int favoriteUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_FAVORITES,
                COLUMN_FAVORITE_USER_ID + " = ? AND " + COLUMN_FAVORITE_FAVORITE_USER_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(favoriteUserId)});
        return rows > 0;
    }

    // Favori kontrolü
    public boolean isFavorite(int userId, int favoriteUserId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES, null,
                COLUMN_FAVORITE_USER_ID + " = ? AND " + COLUMN_FAVORITE_FAVORITE_USER_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(favoriteUserId)},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Kullanıcının favorilerini getir
    public List<Favorite> getFavorites(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Favorite> favorites = new ArrayList<>();

        String query = "SELECT f." + COLUMN_FAVORITE_ID + ", "
                + "f." + COLUMN_FAVORITE_USER_ID + ", "
                + "f." + COLUMN_FAVORITE_FAVORITE_USER_ID + ", "
                + "u." + COLUMN_USER_FIRST_NAME + " || ' ' || u." + COLUMN_USER_LAST_NAME + " as name, "
                + "u." + COLUMN_USER_TC + ", "
                + "f." + COLUMN_FAVORITE_ADDED_AT
                + " FROM " + TABLE_FAVORITES + " f"
                + " INNER JOIN " + TABLE_USERS + " u ON f." + COLUMN_FAVORITE_FAVORITE_USER_ID
                + " = u." + COLUMN_USER_ID
                + " WHERE f." + COLUMN_FAVORITE_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        while (cursor.moveToNext()) {
            Favorite favorite = new Favorite(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            favorites.add(favorite);
        }
        cursor.close();
        return favorites;
    }
}