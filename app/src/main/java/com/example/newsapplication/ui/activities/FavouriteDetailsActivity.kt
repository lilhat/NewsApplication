package com.example.newsapplication.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.newsapplication.ui.adapters.FavouritesDataHelper
import com.example.newsapplication.Models.Headlines
import com.example.newsapplication.R
import com.squareup.picasso.Picasso


class FavouriteDetailsActivity : AppCompatActivity() {
    var headlines = object : Headlines(){}
    private lateinit var titleText: String
    private lateinit var authorText: String
    private lateinit var countryText: String
    private lateinit var categoryText: String
    private lateinit var timeText: String
    private lateinit var textText: String
    private lateinit var imgText: String
    private lateinit var linkText: String
    private lateinit var sourceText: String
    private lateinit var favouritesDataHelper: FavouritesDataHelper

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
        val fullButton = findViewById<Button>(R.id.full_button)
        headlines = intent.getSerializableExtra("data") as Headlines
        titleText = headlines.title

        timeText = headlines.pubDate
        textText = ""
        if(headlines.image_url != null){
            imgText = headlines.image_url
        }

        linkText = headlines.link
        if(headlines.image_url != null){
            imgText = headlines.image_url
        }
        else{
            imgText = "N/A"
        }
        title.text = titleText
        if(headlines.creator != null){
            authorText = headlines.creator!![0].toString()
            author.text = authorText
        }
        else{
            author.text = getString(R.string.unknown_author)
            authorText = getString(R.string.unknown_author)
        }

        categoryText = headlines.category?.get(0).toString().titleCaseFirstChar()
        val countryList = headlines.country?.get(0).toString().split("\\s".toRegex()).toTypedArray()
        countryText = if(countryList.size > 1){
            countryList[0].titleCaseFirstChar() + " " + countryList[1].titleCaseFirstChar()

        } else{
            countryList[0].titleCaseFirstChar()
        }
        category.text = categoryText
        country.text = countryText
        time.text = timeText
        textText = if(headlines.description != null){
            headlines.description
        } else{
            headlines.content
        }
        sourceText = headlines.source_id.toString()
        text.text = textText
        Picasso.get().load(imgText).into(img)
        fullButton.setOnClickListener{
            launchUrl(linkText)
        }
        favouritesDataHelper =
            FavouritesDataHelper(
                this
            )

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favourites_toolbar_layout, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            setResult(RESULT_CANCELED)
            finish()
        }
        else if(item.itemId == R.id.like){
            if(favouritesDataHelper.checkData(titleText)){
                Toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show()
                item.icon = getDrawable(R.drawable.ic_baseline_favorite_border_24)
                favouritesDataHelper.removeData(titleText)

            }
            else{
                item.icon = getDrawable(R.drawable.ic_baseline_favorite_24)
                Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT).show()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun launchUrl(url: String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun String.titleCaseFirstChar() = replaceFirstChar(Char::titlecase)
}
