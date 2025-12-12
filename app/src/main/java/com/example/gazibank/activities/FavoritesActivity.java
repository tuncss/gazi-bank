package com.example.gazibank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gazibank.R;
import com.example.gazibank.database.DatabaseHelper;
import com.example.gazibank.models.Favorite;
import com.example.gazibank.utils.SessionManager;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements FavoriteAdapter.OnFavoriteActionListener {

    private RecyclerView recyclerViewFavorites;
    private LinearLayout layoutEmptyState;
    private Button buttonBack;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private int currentUserId;
    private FavoriteAdapter adapter;
    private List<Favorite> favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Initialize
        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        currentUserId = sessionManager.getUserId();

        // View'ları bağla
        recyclerViewFavorites = findViewById(R.id.recyclerViewFavorites);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        buttonBack = findViewById(R.id.buttonBack);

        // RecyclerView ayarları
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this));

        // Click listeners
        buttonBack.setOnClickListener(v -> finish());

        // Favorileri yükle
        loadFavorites();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ekrana her dönüldüğünde favorileri yenile
        loadFavorites();
    }

    private void loadFavorites() {
        favorites = databaseHelper.getFavorites(currentUserId);

        if (favorites.isEmpty()) {
            // Hiç favori yoksa boş durum göster
            recyclerViewFavorites.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            // Favorileri listele
            recyclerViewFavorites.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);

            adapter = new FavoriteAdapter(favorites, this);
            recyclerViewFavorites.setAdapter(adapter);
        }
    }

    @Override
    public void onSendMoney(Favorite favorite) {
        // Transfer ekranına git ve favori bilgilerini gönder
        Intent intent = new Intent(FavoritesActivity.this, TransferActivity.class);
        intent.putExtra("RECEIVER_TC", favorite.getFavoriteUserTC());
        intent.putExtra("RECEIVER_NAME", favorite.getFavoriteUserName());
        startActivity(intent);
    }

    @Override
    public void onRemoveFavorite(Favorite favorite, int position) {
        // Silme onayı al
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Favoriden Çıkar");
        builder.setMessage(favorite.getFavoriteUserName() + " kişisini favorilerinizden çıkarmak istediğinizden emin misiniz?");

        builder.setPositiveButton("Evet", (dialog, which) -> {
            boolean removed = databaseHelper.removeFavorite(currentUserId, favorite.getFavoriteUserId());

            if (removed) {
                adapter.removeFavorite(position);
                Toast.makeText(this, "Favorilerden çıkarıldı", Toast.LENGTH_SHORT).show();

                // Eğer liste boşaldıysa boş durum göster
                if (adapter.getItemCount() == 0) {
                    recyclerViewFavorites.setVisibility(View.GONE);
                    layoutEmptyState.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this, "Bir hata oluştu", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("İptal", null);
        builder.show();
    }
}