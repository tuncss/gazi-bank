# 🏦 Gazi Bank - Mobil Bankacılık Uygulaması

## 👥 Proje Ekibi

| Öğrenci Adı | Öğrenci No |
|-------------|------------|
| Mehmet Emre Kahraman | 24118080056 |
| Oğuz Giray Gök | 24118080034 |
| İsmail İbiş | 24118080006 |
| Mustafa Tunç | 21118080724 |

---

Gazi Bank, Android platformu için geliştirilmiş yerel (offline) bir bankacılık simülasyon uygulamasıdır. SQLite veritabanı kullanarak kullanıcı hesap yönetimi, para transferi ve işlem geçmişi takibi gibi temel bankacılık işlemlerini gerçekleştirir.

## 🛠️ Teknolojiler

- **Dil:** Java
- **Platform:** Android (Minimum SDK: API 24 / Android 7.0)
- **IDE:** Android Studio
- **Veritabanı:** SQLite
- **Mimari:** OOP (Object-Oriented Programming)
- **UI Bileşenleri:** Material Design Components, CardView, RecyclerView

## 📂 Proje Yapısı

```
GaziBank/
│
├── app/src/main/java/com/example/gazibank/
│   │
│   ├── models/                      # Model Sınıfları (OOP)
│   │   ├── User.java               # Kullanıcı modeli
│   │   ├── Account.java            # Hesap modeli
│   │   ├── Transaction.java        # İşlem modeli
│   │   └── Favorite.java           # Favori modeli
│   │
│   ├── database/                    # Veritabanı İşlemleri
│   │   └── DatabaseHelper.java     # SQLite yöneticisi
│   │
│   ├── activities/                  # Ekranlar
│   │   ├── LoginActivity.java      # Giriş ekranı
│   │   ├── RegisterActivity.java   # Kayıt ekranı
│   │   ├── MainActivity.java       # Ana ekran
│   │   ├── TransferActivity.java   # Transfer ekranı
│   │   ├── TransactionHistoryActivity.java
│   │   ├── FavoritesActivity.java
│   │   ├── ProfileActivity.java
│   │   ├── TransactionAdapter.java # İşlem listesi adaptörü
│   │   └── FavoriteAdapter.java    # Favori listesi adaptörü
│   │
│   └── utils/                       # Yardımcı Sınıflar
│       ├── SessionManager.java     # Oturum yönetimi
│       └── ValidationHelper.java   # Doğrulama işlemleri
│
└── app/src/main/res/
    ├── layout/                      # XML Tasarımları
    ├── values/                      # Renkler, metinler, temalar
    └── drawable/                    # Görseller
```

## 🗄️ Veritabanı Şeması

### Users Tablosu
| Sütun | Tip | Açıklama |
|-------|-----|----------|
| id | INTEGER | Primary Key |
| tc | TEXT | TC Kimlik No (UNIQUE) |
| password | TEXT | Şifre |
| first_name | TEXT | Ad |
| last_name | TEXT | Soyad |
| created_at | DATETIME | Kayıt tarihi |

### Accounts Tablosu
| Sütun | Tip | Açıklama |
|-------|-----|----------|
| id | INTEGER | Primary Key |
| user_id | INTEGER | Foreign Key → users(id) |
| balance | REAL | Bakiye |
| account_number | TEXT | Hesap numarası (UNIQUE) |
| created_at | DATETIME | Oluşturulma tarihi |

### Transactions Tablosu
| Sütun | Tip | Açıklama |
|-------|-----|----------|
| id | INTEGER | Primary Key |
| sender_user_id | INTEGER | Foreign Key → users(id) |
| receiver_user_id | INTEGER | Foreign Key → users(id) |
| amount | REAL | İşlem tutarı |
| transaction_date | DATETIME | İşlem tarihi |
| type | TEXT | İşlem tipi (TRANSFER) |
| description | TEXT | Açıklama |

### Favorites Tablosu
| Sütun | Tip | Açıklama |
|-------|-----|----------|
| id | INTEGER | Primary Key |
| user_id | INTEGER | Foreign Key → users(id) |
| favorite_user_id | INTEGER | Foreign Key → users(id) |
| added_at | DATETIME | Eklenme tarihi |

## 🎯 OOP (Object-Oriented Programming) Prensipleri

Bu proje, nesne yönelimli programlamanın tüm temel prensiplerini kapsamlı bir şekilde uygular. Her prensibin kod içindeki somut örnekleri aşağıda detaylı olarak açıklanmıştır.

---

### 1. Encapsulation (Kapsülleme)

Encapsulation, verilerin gizliliğini ve kontrollü erişimini sağlar. Projede tüm model sınıfları bu prensibi uygular.

#### User.java Örneği:
```java
public class User {
    // Private değişkenler - dışarıdan doğrudan erişilemez
    private int id;
    private String tc;
    private String password;
    private String firstName;
    private String lastName;
    private String createdAt;

    // Public getter metodları - okuma erişimi
    public int getId() { return id; }
    public String getTc() { return tc; }
    public String getPassword() { return password; }
    public String getFullName() { return firstName + " " + lastName; }

    // Public setter metodları - yazma erişimi (kontrollü)
    public void setPassword(String password) { this.password = password; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
}
```

**Neden önemli?**
- `password` değişkeni private olduğu için dışarıdan doğrudan değiştirilemez
- Sadece `setPassword()` metodu ile kontrollü şekilde güncellenebilir
- `getFullName()` gibi hesaplanmış değerler sunulabilir
- Veri bütünlüğü korunur

#### Account.java'da İş Mantığı ile Encapsulation:
```java
public class Account {
    private double balance;  // Bakiye gizli

    // Para çekme - kontrollü erişim
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;  // Yetersiz bakiye
    }

    // Para yatırma - kontrollü erişim
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    // Bakiye kontrolü - sadece okuma
    public boolean hasSufficientBalance(double amount) {
        return balance >= amount;
    }
}
```

**Avantajlar:**
- `balance` değişkenine doğrudan erişim yok
- İş kuralları (negatif tutar kontrolü) metodların içinde
- Tutarsız veri durumları önlenir

#### Diğer Örnekler:
- **Transaction.java:** `amount`, `senderUserId`, `receiverUserId` private
- **Favorite.java:** `userId`, `favoriteUserId` private
- **SessionManager.java:** SharedPreferences üzerinden private veri saklama

---

### 2. Abstraction (Soyutlama)

Abstraction, karmaşık işlemleri basit arayüzler arkasında gizler. Kullanıcı detayları bilmeden işlem yapabilir.

#### DatabaseHelper.java'da Abstraction:
```java
// Karmaşık SQL işlemi basit metod olarak sunuluyor
public boolean transferMoney(int senderId, int receiverId, double amount, String description) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.beginTransaction();
    try {
        // Gönderenin bakiyesini kontrol et
        Account senderAccount = getAccountByUserId(senderId);
        if (!senderAccount.hasSufficientBalance(amount)) {
            return false;
        }
        
        // Bakiyeleri güncelle
        updateBalance(senderId, senderAccount.getBalance() - amount);
        updateBalance(receiverId, receiverAccount.getBalance() + amount);
        
        // İşlemi kaydet
        ContentValues values = new ContentValues();
        values.put("sender_user_id", senderId);
        values.put("receiver_user_id", receiverId);
        values.put("amount", amount);
        db.insert("transactions", null, values);
        
        db.setTransactionSuccessful();
        return true;
    } finally {
        db.endTransaction();
    }
}
```

**Kullanımı (TransferActivity.java):**
```java
// Kullanıcı SQL detaylarını bilmiyor, sadece basit metod çağırıyor
boolean success = databaseHelper.transferMoney(
    currentUserId,
    receiverUser.getId(),
    amount,
    description
);
```

#### Diğer Abstraction Örnekleri:

**getUserByTC():**
```java
// SQL sorgusu gizli
public User getUserByTC(String tc) {
    // Karmaşık Cursor işlemleri içeride
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.query(TABLE_USERS, null, 
        COLUMN_USER_TC + " = ?", new String[]{tc}, 
        null, null, null);
    // ...
}
```

**ValidationHelper.java:**
```java
// TC validasyon algoritması gizli
public static boolean isValidTC(String tc) {
    if (tc == null || tc.length() != 11) return false;
    try {
        Long.parseLong(tc);
        if (tc.charAt(0) == '0') return false;
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}
```

**SessionManager.java:**
```java
// SharedPreferences detayları gizli
public void createLoginSession(int userId, String tc, String fullName) {
    editor.putBoolean(KEY_IS_LOGGED_IN, true);
    editor.putInt(KEY_USER_ID, userId);
    editor.commit();
}
```

---

### 3. Inheritance (Kalıtım)

Inheritance, kod tekrarını önler ve ortak özellikleri miras almayı sağlar.

#### Activity Sınıflarında Kalıtım:
```java
// Tüm Activity'ler AppCompatActivity'den türetilmiş
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  // Üst sınıfın metodunu çağırma
        setContentView(R.layout.activity_login);
    }
}

public class MainActivity extends AppCompatActivity { /* ... */ }
public class TransferActivity extends AppCompatActivity { /* ... */ }
public class ProfileActivity extends AppCompatActivity { /* ... */ }
```

**Miras alınan özellikler:**
- `onCreate()`, `onResume()`, `onPause()` lifecycle metodları
- `findViewById()` metodu
- `setContentView()` metodu
- `startActivity()` metodu
- Context erişimi
- Intent yönetimi
- Menu işlemleri

#### RecyclerView.Adapter Kalıtımı:
```java
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Üst sınıfın abstract metodunu implement ediyoruz
    }
    
    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        // Her item için veri bağlama
    }
    
    @Override
    public int getItemCount() {
        return transactions.size();
    }
}

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    // Aynı yapı, farklı veri tipi
}
```

#### SQLiteOpenHelper Kalıtımı:
```java
public class DatabaseHelper extends SQLiteOpenHelper {
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Veritabanı ilk oluşturulurken çağrılır
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Veritabanı versiyonu güncellenirken çağrılır
    }
}
```

---

### 4. Polymorphism (Çok Biçimlilik)

Polymorphism, aynı isimli metodların farklı şekillerde davranmasını sağlar.

#### Constructor Overloading (Yapıcı Metod Aşırı Yüklemesi)

**User.java:**
```java
// Yeni kullanıcı oluşturmak için (id yok)
public User(String tc, String password, String firstName, String lastName) {
    this.tc = tc;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
}

// Veritabanından okumak için (id var)
public User(int id, String tc, String password, String firstName, 
            String lastName, String createdAt) {
    this.id = id;
    this.tc = tc;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.createdAt = createdAt;
}
```

**Account.java:**
```java
// Yeni hesap oluşturma
public Account(int userId, double balance, String accountNumber) {
    this.userId = userId;
    this.balance = balance;
    this.accountNumber = accountNumber;
}

// Veritabanından veri okuma
public Account(int id, int userId, double balance, String accountNumber, String createdAt) {
    this.id = id;
    this.userId = userId;
    this.balance = balance;
    this.accountNumber = accountNumber;
    this.createdAt = createdAt;
}
```

**Transaction.java:**
```java
// İki farklı constructor
public Transaction(int senderUserId, int receiverUserId, double amount, 
                   String type, String description) { }

public Transaction(int id, int senderUserId, int receiverUserId, double amount, 
                   String transactionDate, String type, String description) { }
```

#### Method Overriding (Metod Ezme)

**toString() Override:**
```java
// User.java
@Override
public String toString() {
    return "User{id=" + id + ", tc='" + tc + "', firstName='" + firstName + "'}";
}

// Account.java
@Override
public String toString() {
    return "Account{id=" + id + ", balance=" + balance + "}";
}

// Transaction.java
@Override
public String toString() {
    return "Transaction{id=" + id + ", amount=" + amount + "}";
}
```

**Activity Lifecycle Override:**
```java
// MainActivity.java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Kendi implementasyonumuz
}

@Override
protected void onResume() {
    super.onResume();
    loadUserData();  // Her dönüldüğünde verileri yenile
}

@Override
public void onBackPressed() {
    super.onBackPressed();
    finishAffinity();  // Uygulamadan çık
}
```

#### Runtime Polymorphism (Çalışma Zamanı Çok Biçimliliği)

**DatabaseHelper.java:**
```java
// insertUser metodu hem private hem public olarak kullanılıyor
private long insertUser(SQLiteDatabase db, String tc, String password, 
                       String firstName, String lastName) {
    // Test verileri için
}

public boolean registerUser(String firstName, String lastName, String tc, 
                           String password, double initialBalance) {
    // Kullanıcı kaydı için
}
```

---

### 5. Composition (Bileşim/Birleştirme)

Composition, "has-a" ilişkisini temsil eder. Bir sınıf başka sınıfların nesnelerini içerir.

#### Activity'lerde Composition:

**MainActivity.java:**
```java
public class MainActivity extends AppCompatActivity {
    // MainActivity "has-a" DatabaseHelper
    private DatabaseHelper databaseHelper;
    
    // MainActivity "has-a" SessionManager
    private SessionManager sessionManager;
    
    // MainActivity "has-a" Account
    private Account currentAccount;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
    }
}
```

**TransferActivity.java:**
```java
public class TransferActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;  // Veritabanı erişimi
    private SessionManager sessionManager;   // Oturum yönetimi
    private Account currentAccount;          // Mevcut hesap
    private User receiverUser;               // Alıcı kullanıcı
}
```

**TransactionHistoryActivity.java:**
```java
public class TransactionHistoryActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private TransactionAdapter adapter;  // Adapter bileşimi
    private List<Transaction> transactions;  // Transaction listesi
}
```

#### Adapter Pattern ile Composition:

**TransactionAdapter.java:**
```java
public class TransactionAdapter extends RecyclerView.Adapter {
    private List<Transaction> transactions;  // Transaction nesnelerini içerir
    private DatabaseHelper databaseHelper;   // Database helper'ı içerir
    
    public TransactionAdapter(List<Transaction> transactions, int currentUserId, 
                             DatabaseHelper databaseHelper) {
        this.transactions = transactions;
        this.databaseHelper = databaseHelper;
    }
}
```

---

### 6. Interface (Arayüz)

Interface, sınıflar arasında sözleşme (contract) tanımlar.

**FavoriteAdapter.java:**
```java
public class FavoriteAdapter extends RecyclerView.Adapter {
    
    // Interface tanımı
    public interface OnFavoriteActionListener {
        void onSendMoney(Favorite favorite);
        void onRemoveFavorite(Favorite favorite, int position);
    }
    
    private OnFavoriteActionListener listener;
    
    public FavoriteAdapter(List<Favorite> favorites, OnFavoriteActionListener listener) {
        this.listener = listener;
    }
    
    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        holder.buttonSendMoney.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSendMoney(favorite);  // Interface metodunu çağır
            }
        });
    }
}
```

**FavoritesActivity.java (Interface Implementation):**
```java
public class FavoritesActivity extends AppCompatActivity 
        implements FavoriteAdapter.OnFavoriteActionListener {
    
    @Override
    public void onSendMoney(Favorite favorite) {
        // Interface metodunu implement et
        Intent intent = new Intent(this, TransferActivity.class);
        intent.putExtra("RECEIVER_TC", favorite.getFavoriteUserTC());
        startActivity(intent);
    }
    
    @Override
    public void onRemoveFavorite(Favorite favorite, int position) {
        // Interface metodunu implement et
        boolean removed = databaseHelper.removeFavorite(currentUserId, 
                                                       favorite.getFavoriteUserId());
        if (removed) {
            adapter.removeFavorite(position);
        }
    }
}
```

**Neden Interface?**
- Adapter ve Activity arasında gevşek bağlantı (loose coupling)
- Adapter'ı başka Activity'lerde de kullanabilme
- Test edilebilirlik

---

### 7. Single Responsibility Principle (Tek Sorumluluk İlkesi)

Her sınıf tek bir sorumluluğa sahip olmalıdır.

**Sınıf Sorumlulukları:**

| Sınıf | Sorumluluğu |
|-------|-------------|
| **User.java** | Sadece kullanıcı verilerini temsil eder |
| **Account.java** | Sadece hesap verilerini ve bakiye işlemlerini yönetir |
| **Transaction.java** | Sadece işlem verilerini temsil eder |
| **DatabaseHelper.java** | Sadece veritabanı CRUD işlemlerini yapar |
| **SessionManager.java** | Sadece kullanıcı oturumu yönetir |
| **ValidationHelper.java** | Sadece veri doğrulama işlemleri yapar |
| **TransactionAdapter.java** | Sadece işlem listesini RecyclerView'da gösterir |
| **LoginActivity.java** | Sadece giriş ekranı mantığını yönetir |

**ValidationHelper.java Örneği:**
```java
public class ValidationHelper {
    // Sadece validasyon işlemleri
    public static boolean isValidTC(String tc) { }
    public static boolean isValidPassword(String password) { }
    public static boolean isValidName(String name) { }
    public static boolean isValidAmount(String amountStr) { }
    public static String formatAmount(double amount) { }
}
```

**SessionManager.java Örneği:**
```java
public class SessionManager {
    // Sadece oturum yönetimi
    public void createLoginSession(int userId, String tc, String fullName) { }
    public void logoutUser() { }
    public boolean isLoggedIn() { }
    public int getUserId() { }
}
```

---

### 8. Static Methods (Statik Metodlar)

Nesne oluşturmadan kullanılabilen yardımcı metodlar.

**ValidationHelper.java:**
```java
public class ValidationHelper {
    // Static metodlar - nesne oluşturmaya gerek yok
    public static boolean isValidTC(String tc) {
        if (tc == null || tc.length() != 11) return false;
        try {
            Long.parseLong(tc);
            return tc.charAt(0) != '0';
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static String getText(EditText editText) {
        return editText.getText().toString().trim();
    }
    
    public static void showError(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
    }
}
```

**Kullanımı:**
```java
// Nesne oluşturmadan kullanım
if (ValidationHelper.isValidTC(tc)) {
    // ...
}

String text = ValidationHelper.getText(editTextTC);
ValidationHelper.showError(editTextTC, "Hata mesajı");
```

**User.java'da Static Validasyon:**
```java
public class User {
    // Static utility method
    public static boolean isValidTC(String tc) {
        if (tc == null || tc.length() != 11) return false;
        try {
            Long.parseLong(tc);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
```

---

### 9. Data Hiding (Veri Gizleme)

Private değişkenler ve metodlar ile veri gizleme.

**DatabaseHelper.java:**
```java
public class DatabaseHelper extends SQLiteOpenHelper {
    // Private sabitler - dışarıdan erişilemez
    private static final String DATABASE_NAME = "GaziBank.db";
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    
    // Private helper metodlar
    private long insertUser(SQLiteDatabase db, String tc, String password, 
                           String firstName, String lastName) {
        // Sadece DatabaseHelper içinden kullanılır
    }
    
    private long insertAccount(SQLiteDatabase db, long userId, double balance, 
                              String accountNumber) {
        // Sadece DatabaseHelper içinden kullanılır
    }
    
    // Public metodlar - dışarıya açık API
    public boolean registerUser(String firstName, String lastName, String tc, 
                               String password, double initialBalance) {
        // Public - herkes kullanabilir
    }
}
```

**SessionManager.java:**
```java
public class SessionManager {
    // Private sabitler - dışarıdan erişilemez
    private static final String PREF_NAME = "GaziBankSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    
    private SharedPreferences preferences;  // Private değişken
    private SharedPreferences.Editor editor;  // Private değişken
    
    // Public metodlar ile kontrollü erişim
    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
```

---

### OOP Prensiplerinin Faydaları

✅ **Modülerlik:** Her sınıf bağımsız çalışır
✅ **Yeniden Kullanılabilirlik:** Kod tekrarı minimum
✅ **Bakım Kolaylığı:** Değişiklikler izole edilmiş
✅ **Test Edilebilirlik:** Her sınıf ayrı test edilebilir
✅ **Güvenlik:** Veri gizleme ile koruma
✅ **Genişletilebilirlik:** Yeni özellikler kolayca eklenir
✅ **Okunabilirlik:** Kod mantıksal olarak organize

---

## 🚀 Kurulum ve Çalıştırma

### Gereksinimler
- Android Studio (Arctic Fox veya üzeri)
- JDK 8 veya üzeri
- Android SDK (API 24+)
- Emulator veya fiziksel Android cihaz

### Adımlar

1. **Projeyi klonlayın:**
```bash
git clone https://github.com/kullaniciadi/gazi-bank.git
cd gazi-bank
```

2. **Android Studio'da açın:**
   - Android Studio → File → Open
   - Proje klasörünü seçin

3. **Gradle Sync:**
   - Android Studio otomatik olarak bağımlılıkları indirecektir
   - Hata alırsanız: File → Sync Project with Gradle Files

4. **Çalıştırın:**
   - Emulator veya fiziksel cihaz seçin
   - Run düğmesine tıklayın (Shift+F10)

## 👥 Test Kullanıcıları

Uygulama ilk kurulumda 3 test kullanıcısı ile gelir:

| TC Kimlik No | Şifre | Bakiye |
|-------------|-------|--------|
| 12345678901 | 123456 | 5000 TL |
| 98765432109 | 123456 | 3000 TL |
| 11111111111 | 123456 | 10000 TL |

**Yeni kullanıcı da oluşturabilirsiniz:**
- Giriş ekranında "Kayıt Ol" linkine tıklayın
- Formu doldurun ve hesap oluşturun

---
