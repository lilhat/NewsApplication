package com.example.newsapplication.adapters

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.models.Headlines
import com.example.newsapplication.R
import com.example.newsapplication.interfaces.SelectListener
import com.example.newsapplication.viewholders.CustomViewHolder
import com.squareup.picasso.Picasso


// Custom adapter to place the articles into the view-holder
class CustomAdapter(
    private val context: Context,
    headlines: MutableList<Headlines>,
    listener: SelectListener
) :
    RecyclerView.Adapter<CustomViewHolder>() {
    private var headlines: MutableList<Headlines>
    private val listener: SelectListener

    init {
        this.headlines = removeInvalidData(headlines)
        this.listener = listener
    }

    // Adding more news articles when end of page is reached
    @SuppressLint("NotifyDataSetChanged")
    fun addNews(newHeadlines: MutableList<Headlines>?) {
        headlines.addAll(removeInvalidData(newHeadlines!!))
        notifyDataSetChanged()
    }

    // Inflate headline items layout after each view is set
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.headline_items, parent, false)
        return CustomViewHolder(view)
    }

    // Remove any headlines that do not contain and image or a description
    @SuppressLint("NotifyDataSetChanged")
    private fun removeInvalidData(headlines: MutableList<Headlines>): MutableList<Headlines> {
        val found = mutableListOf<Headlines>()
        if(headlines.size != 0){
            for(position in 0 until headlines.size) {
                if (headlines[position].image_url == null || headlines[position].image_url!!.isEmpty()) {
                    Log.d(TAG, "Removed due to image url")
                    found.add(headlines[position])
                } else if (headlines[position].description == null || headlines[position].description!!.isEmpty()) {
                    if (headlines[position].content == null || headlines[position].content!!.isEmpty()){
                        Log.d(TAG, "Removed due to content")
                        found.add(headlines[position])
                    }
                }
            }
        }
        headlines.removeAll(found)
        notifyDataSetChanged()
        return headlines
    }

    // Binding each view in the holder with its associated content
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        Picasso.get().load(headlines[position].image_url)
            .into(holder.news_img)
        holder.news_title.text = headlines[position].title
        holder.news_source.text = headlines[position].source_id
        if (headlines[position].description != null && headlines[position].description!!.isNotEmpty()) {
            holder.news_text.text = headlines[position].description
        } else {
            holder.news_text.text = headlines[position].content
        }
        if (headlines[position].creator != null) {
            val creator = headlines[position].creator
            holder.news_creator.text = creator?.get(0).toString()
        } else {
            holder.news_creator.text = context.getString(R.string.unknown_author)
        }
        holder.cardView.setOnClickListener { listener.OnNewsClicked(headlines[holder.absoluteAdapterPosition]) }
    }



    // Return the size of the list
    override fun getItemCount(): Int {
        return headlines.size
    }
}