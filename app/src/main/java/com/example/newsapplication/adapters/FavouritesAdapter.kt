package com.example.newsapplication.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.models.Headlines
import com.example.newsapplication.R
import com.example.newsapplication.interfaces.SelectListener
import com.example.newsapplication.utils.FavouritesDataHelper
import com.example.newsapplication.viewholders.FavouritesViewHolder
import com.squareup.picasso.Picasso

// Adapter to retrieve data from SQLite database to be placed into view-holder
class FavouritesAdapter(private val mContext: Context, private val mCursor: Cursor, private val listener: SelectListener) :
    RecyclerView.Adapter<FavouritesViewHolder>() {

    // Inflate favourite items layout after each view is set
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        return FavouritesViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.favourite_items, parent, false)
        )
    }

    // Binding each view in the holder with its associated content
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
        @SuppressLint("Range") val creator =
            mCursor.getString(mCursor.getColumnIndex(FavouritesDataHelper.COL_CREATOR))
        @SuppressLint("Range") val category =
            mCursor.getString(mCursor.getColumnIndex(FavouritesDataHelper.COL_CATEGORY))
        @SuppressLint("Range") val link =
            mCursor.getString(mCursor.getColumnIndex(FavouritesDataHelper.COL_LINK))
        @SuppressLint("Range") val country =
            mCursor.getString(mCursor.getColumnIndex(FavouritesDataHelper.COL_COUNTRY))
        @SuppressLint("Range") val time =
            mCursor.getString(mCursor.getColumnIndex(FavouritesDataHelper.COL_TIME))
        @SuppressLint("Range") val image =
            mCursor.getString(mCursor.getColumnIndex(FavouritesDataHelper.COL_IMG))

        val headlines = object: Headlines(){
        }
        headlines.title = title
        headlines.source_id = source
        headlines.creator = listOf<String>(creator)
        headlines.category = listOf<String>(category)
        headlines.country = listOf<String>(country)
        headlines.link = link
        headlines.description = text
        headlines.pubDate = time
        headlines.image_url = image

        holder.titleText.text = title
        holder.sourceText.text = source
        holder.textText.text = text
        holder.creatorText.text = creator
        Picasso.get().load(image).into(holder.newsImage)
        holder.cardView.setOnClickListener { listener.OnNewsClicked(headlines) }
    }


    // Return the size of the data in database
    override fun getItemCount(): Int {
        return mCursor.count
    }
}