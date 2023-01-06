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
import com.example.newsapplication.utils.FavouritesDataHelper
import com.example.newsapplication.models.Headlines
import com.example.newsapplication.R
import com.squareup.picasso.Picasso

// Details for when a favourite article is clicked
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

    // Setting all views with data retrieved from intent
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

        linkText = headlines.link
        imgText = if(headlines.image_url != null){
            headlines.image_url!!
        } else{
            getString(R.string.not_available)
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

        if(headlines.description != null){
            textText = headlines.description!!
        } else if (headlines.content != null){
            textText = headlines.content!!
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

    // Inflating the unique toolbar layout for favourites
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favourites_toolbar_layout, menu)
        return true
    }

    // Setting actions for when each toolbar item is clicked
    // If like item is clicked then remove data from database
    // If share item is clicked then launch intent to choose share destination
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
                Toast.makeText(this, "Could not be saved", Toast.LENGTH_SHORT).show()
            }

        }
        else if (item.itemId == R.id.share){
            val shareText: String = linkText
            val myShareIntent = Intent(Intent.ACTION_SEND)
            myShareIntent.type = "text/plain"
            myShareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            startActivity(Intent.createChooser(myShareIntent, "Share to: "))
        }
        return super.onOptionsItemSelected(item)
    }

    // Launching browser intent with provided link to view full content
    private fun launchUrl(url: String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    // Function to replace string first character with uppercase
    private fun String.titleCaseFirstChar() = replaceFirstChar(Char::titlecase)
}
