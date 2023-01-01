package com.example.newsapplication.ui.adapters

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.R

class FavouritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var titleText: TextView
    var sourceText: TextView
    var creatorText: TextView
    var categoryText: TextView? = null
    var countryText: TextView? = null
    var timeText: TextView? = null
    var textText: TextView
    var newsImage: ImageView
    var cardView: CardView
    var moreText: TextView? = null

    init {
        titleText = itemView.findViewById(R.id.news_title)
        sourceText = itemView.findViewById(R.id.news_source)
        creatorText = itemView.findViewById(R.id.news_creator)
        textText = itemView.findViewById(R.id.news_text)
        newsImage = itemView.findViewById(R.id.news_img)
        cardView = itemView.findViewById(R.id.main_container)

    }
}