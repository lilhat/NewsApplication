package com.example.newsapplication.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.newsapplication.Models.Headlines
import com.example.newsapplication.R
import com.squareup.picasso.Picasso

class DetailsActivity : AppCompatActivity() {
    var headlines = object : Headlines(){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val title = findViewById<TextView>(R.id.detail_title)
        val author = findViewById<TextView>(R.id.detail_author)
        val country = findViewById<TextView>(R.id.detail_country)
        val category = findViewById<TextView>(R.id.detail_category)
        val time = findViewById<TextView>(R.id.detail_time)
        val text = findViewById<TextView>(R.id.detail_text)
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
        if(headlines.description != null){
            text.text = headlines.description
        }
        else{
            text.text = headlines.content
        }

        Picasso.get().load(headlines.image_url).into(img)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            setResult(RESULT_CANCELED)
            finish()
        }
        return true
    }
    private fun String.titleCaseFirstChar() = replaceFirstChar(Char::titlecase)
}
