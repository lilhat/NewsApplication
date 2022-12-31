package com.example.newsapplication.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapplication.R;
import com.squareup.picasso.Picasso;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public FavouritesAdapter(Context context, Cursor cursor){
        this.mContext = context;
        this.mCursor = cursor;
    }


    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavouritesViewHolder(LayoutInflater.from(mContext).inflate(R.layout.favourite_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position) {
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
