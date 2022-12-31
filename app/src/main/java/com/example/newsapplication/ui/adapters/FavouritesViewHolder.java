package com.example.newsapplication.ui.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapplication.R;

public class FavouritesViewHolder extends RecyclerView.ViewHolder{
    public TextView titleText;
    public TextView sourceText;
    public TextView categoryText;
    public TextView countryText;
    public TextView timeText;
    public TextView textText;
    public ImageView newsImage;
    public CardView cardView;
    public TextView moreText;

    public FavouritesViewHolder(@NonNull View itemView) {
        super(itemView);
        titleText = itemView.findViewById(R.id.news_title);
        sourceText = itemView.findViewById(R.id.news_source);
        textText = itemView.findViewById(R.id.news_text);
        newsImage = itemView.findViewById(R.id.news_img);
        cardView = itemView.findViewById(R.id.main_container);

    }
}
