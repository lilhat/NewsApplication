package com.example.newsapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.newsapplication.Models.Headlines
import com.squareup.picasso.Picasso

class DetailsActivity : AppCompatActivity() {
    var headlines = object : Headlines(){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val detail_title = findViewById<TextView>(R.id.detail_title)
        val detail_author = findViewById<TextView>(R.id.detail_author)
        val detail_time = findViewById<TextView>(R.id.detail_time)
        val detail_headline = findViewById<TextView>(R.id.detail_headline)
        val detail_img = findViewById<ImageView>(R.id.detail_img)
        headlines = intent.getSerializableExtra("data") as Headlines

        detail_title.text = headlines.title
        detail_author.text = headlines.source_id
        detail_time.text = headlines.pubDate
        detail_headline.text = headlines.description
        Picasso.get().load(headlines.image_url).into(detail_img)


    }
}