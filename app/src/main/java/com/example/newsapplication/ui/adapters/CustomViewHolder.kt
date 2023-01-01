package com.example.newsapplication.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.R

class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var news_title: TextView
    var news_source: TextView
    var news_text: TextView
    var news_creator: TextView
    var news_img: ImageView
    var cardView: CardView

    init {
        news_title = itemView.findViewById(R.id.news_title)
        news_source = itemView.findViewById(R.id.news_source)
        news_text = itemView.findViewById(R.id.news_text)
        news_creator = itemView.findViewById(R.id.news_creator)
        news_img = itemView.findViewById(R.id.news_img)
        cardView = itemView.findViewById(R.id.main_container)
    }
}