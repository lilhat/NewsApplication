package com.example.newsapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.CustomAdapter
import com.example.newsapplication.FavouritesAdapter
import com.example.newsapplication.FavouritesDataHelper
import com.example.newsapplication.Models.Headlines
import com.example.newsapplication.R
import com.example.newsapplication.RequestManager
import com.example.newsapplication.SelectListener
import com.example.newsapplication.ui.DetailsActivity

class FavouriteFragment:Fragment(R.layout.fragment_favourite), SelectListener, View.OnClickListener {


    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        setupRecyclerView()
        val progressBar = view.findViewById<ProgressBar>(R.id.idPBLoading)
        progressBar.visibility = View.INVISIBLE
        return
    }

    private fun setupRecyclerView(){
        val favouritesDataHelper = FavouritesDataHelper(activity)
        val cursor = favouritesDataHelper.favouriteData
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_main)
        val adapter = FavouritesAdapter(activity, cursor)
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(activity, 1)
            recyclerView.adapter = adapter
        }
    }

    override fun OnNewsClicked(headlines: Headlines?) {
        val myIntent = Intent(context, DetailsActivity::class.java)
        myIntent.putExtra("data", headlines)
        context?.startActivity(myIntent)

    }

    override fun onClick(p0: View?) {
        val progressBar = view?.findViewById<ProgressBar>(R.id.idPBLoading)
        progressBar?.visibility = View.VISIBLE

    }

}