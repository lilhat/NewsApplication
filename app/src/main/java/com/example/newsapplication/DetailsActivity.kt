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
        val title = findViewById<TextView>(R.id.detail_title)
        val author = findViewById<TextView>(R.id.detail_author)
        val country = findViewById<TextView>(R.id.detail_country)
        val category = findViewById<TextView>(R.id.detail_category)
        val time = findViewById<TextView>(R.id.detail_time)
        val headline = findViewById<TextView>(R.id.detail_headline)
        val img = findViewById<ImageView>(R.id.detail_img)
        val countryString: String?
        headlines = intent.getSerializableExtra("data") as Headlines

        title.text = headlines.title
        if(headlines.creator != null){
            author.text = headlines.creator[0].toString()
        }
        else{
            author.text = getString(R.string.unknown_author)
        }

        val categoryString = headlines.category[0].toString().titleCaseFirstChar()
        val countryList = headlines.country[0].toString().split("\\s".toRegex()).toTypedArray()
        countryString = if(countryList.size > 1){
            countryList[0].titleCaseFirstChar() + " " + countryList[1].titleCaseFirstChar()

        } else{
            countryList[0].titleCaseFirstChar()
        }
        category.text = categoryString
        country.text = countryString
        time.text = headlines.pubDate
        headline.text = headlines.description
        Picasso.get().load(headlines.image_url).into(img)


    }

    private fun String.titleCaseFirstChar() = replaceFirstChar(Char::titlecase)
}
