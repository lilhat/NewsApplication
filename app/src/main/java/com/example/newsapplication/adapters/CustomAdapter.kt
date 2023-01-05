package com.example.newsapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.models.Headlines
import com.example.newsapplication.R
import com.example.newsapplication.interfaces.SelectListener
import com.example.newsapplication.viewholders.CustomViewHolder
import com.squareup.picasso.Picasso

class CustomAdapter(
    private val context: Context,
    headlines: MutableList<Headlines>,
    listener: SelectListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val headlines: MutableList<Headlines>?
    private val listener: SelectListener

    init {
        this.headlines = removeInvalidData(headlines)
        this.listener = listener
    }

    fun addNews(newHeadlines: List<Headlines>?) {
        headlines!!.addAll(newHeadlines!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.headline_items, parent, false)
        return CustomViewHolder(view)
    }

    private fun removeInvalidData(headlines: MutableList<Headlines>): MutableList<Headlines> {
        for(position in 0..itemCount) {
            if (headlines[position].image_url == null || headlines[position].image_url.isEmpty()) {
                headlines.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
            } else if (headlines[position].description == null || headlines[position].description.isEmpty()) {
                if (headlines[position].content == null || headlines[position].content.isEmpty()) {
                    headlines.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount)
                }
            }
        }
        return headlines
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Picasso.get().load(headlines!![position].image_url)
            .into((holder as CustomViewHolder).news_img)
        holder.news_title.text = headlines[position].title
        holder.news_source.text = headlines[position].source_id
        if (headlines[position].description != null && headlines[position].description.isNotEmpty()) {
            holder.news_text.text = headlines[position].description
        } else {
            holder.news_text.text = headlines[position].content
        }
        if (headlines[position].creator != null) {
            val creator = headlines[position].creator
            holder.news_creator.text = creator?.get(0).toString()
        } else {
            holder.news_creator.text = "Unknown Author"
        }
        holder.cardView.setOnClickListener { listener.OnNewsClicked(headlines[holder.absoluteAdapterPosition]) }
    }

    override fun getItemCount(): Int {
        return headlines?.size ?: 0
    }
}