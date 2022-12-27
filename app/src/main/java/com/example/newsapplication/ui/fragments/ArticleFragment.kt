package com.example.newsapplication.ui.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.newsapplication.Models.Headlines
import com.example.newsapplication.R
import com.example.newsapplication.RequestManager
import com.squareup.picasso.Picasso

class ArticleFragment:Fragment(R.layout.fragment_article) {
    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val title = view?.findViewById<TextView>(R.id.detail_title)
        val author = view?.findViewById<TextView>(R.id.detail_author)
        val country = view?.findViewById<TextView>(R.id.detail_country)
        val category = view?.findViewById<TextView>(R.id.detail_category)
        val time = view?.findViewById<TextView>(R.id.detail_time)
        val text = view?.findViewById<TextView>(R.id.detail_text)
        val img = view?.findViewById<ImageView>(R.id.detail_img)
        val countryString: String?
        var headlines: String? = "";

        if (arguments != null) {
            headlines = arguments?.getString("Headlines")
        }
        if (headlines != null) {
            Log.d(TAG,headlines)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}