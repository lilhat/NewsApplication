package com.example.newsapplication.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.R
import com.squareup.picasso.Picasso

class FavouritesAdapter(private val mContext: Context, private val mCursor: Cursor) :
    RecyclerView.Adapter<FavouritesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        return FavouritesViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.favourite_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        if (!mCursor.moveToPosition(position)) {
            return
        }
        @SuppressLint("Range") val title =
            mCursor.getString(mCursor.getColumnIndex(FavouritesDataHelper.COL_TITLE))
        @SuppressLint("Range") val source =
            mCursor.getString(mCursor.getColumnIndex(FavouritesDataHelper.COL_SOURCE))
        @SuppressLint("Range") val text =
            mCursor.getString(mCursor.getColumnIndex(FavouritesDataHelper.COL_TEXT))
        @SuppressLint("Range") val image =
            mCursor.getString(mCursor.getColumnIndex(FavouritesDataHelper.COL_IMG))
        holder.titleText.text = title
        holder.sourceText.text = source
        holder.textText.text = text
        Picasso.get().load(image).into(holder.newsImage)
    }

    override fun getItemCount(): Int {
        return mCursor.count
    }
}