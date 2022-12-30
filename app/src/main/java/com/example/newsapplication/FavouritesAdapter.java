package com.example.newsapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public FavouritesAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    public class FavouritesViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText;
        public TextView sourceText;
        public TextView categoryText;
        public TextView countryText;
        public TextView timeText;
        public TextView textText;
        public ImageView newsImage;
        public CardView cardView;

        public FavouritesViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.news_title);
            sourceText = itemView.findViewById(R.id.news_source);
            textText = itemView.findViewById(R.id.news_text);
            newsImage = itemView.findViewById(R.id.news_img);
            cardView = itemView.findViewById(R.id.main_container);

        }
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.headline_items, parent, false);
        return new FavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesAdapter.FavouritesViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)){
            return;
        }
        @SuppressLint("Range") String title = mCursor.getString(mCursor.getColumnIndex(FavouritesDataHelper.COL_TITLE));
        @SuppressLint("Range") String source = mCursor.getString(mCursor.getColumnIndex(FavouritesDataHelper.COL_SOURCE));
        @SuppressLint("Range") String text = mCursor.getString(mCursor.getColumnIndex(FavouritesDataHelper.COL_TEXT));
        @SuppressLint("Range") String image = mCursor.getString(mCursor.getColumnIndex(FavouritesDataHelper.COL_IMG));

        holder.titleText.setText(title);
        holder.sourceText.setText(source);
        holder.textText.setText(text);
        Picasso.get().load(image).into(holder.newsImage);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}
