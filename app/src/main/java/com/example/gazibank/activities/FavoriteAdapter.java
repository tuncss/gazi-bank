package com.example.gazibank.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gazibank.R;
import com.example.gazibank.models.Favorite;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<Favorite> favorites;
    private OnFavoriteActionListener listener;

    public interface OnFavoriteActionListener {
        void onSendMoney(Favorite favorite);
        void onRemoveFavorite(Favorite favorite, int position);
    }

    public FavoriteAdapter(List<Favorite> favorites, OnFavoriteActionListener listener) {
        this.favorites = favorites;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Favorite favorite = favorites.get(position);

        holder.textViewName.setText(favorite.getFavoriteUserName());
        holder.textViewTC.setText("TC: " + favorite.getFavoriteUserTC());

        holder.buttonSendMoney.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSendMoney(favorite);
            }
        });

        holder.buttonRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveFavorite(favorite, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public void removeFavorite(int position) {
        favorites.remove(position);
        notifyItemRemoved(position);
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewTC;
        Button buttonSendMoney;
        Button buttonRemove;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewFavoriteName);
            textViewTC = itemView.findViewById(R.id.textViewFavoriteTC);
            buttonSendMoney = itemView.findViewById(R.id.buttonSendMoney);
            buttonRemove = itemView.findViewById(R.id.buttonRemoveFavorite);
        }
    }
}