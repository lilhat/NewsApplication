package com.example.newsapplication

import FavouriteFragment
import PreferredFragment
import RecommendedFragment
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.Models.ApiResponse
import com.example.newsapplication.Models.Headlines
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        setSupportActionBar(mToolbar)
        val preferredFragment=PreferredFragment()
        val recommendedFragment=RecommendedFragment()
        val favouriteFragment=FavouriteFragment()

        val manager = RequestManager(this)
        manager.getNewsHeadlines(listener, null, "cats")

        setCurrentFragment(preferredFragment)


        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.preferred->setCurrentFragment(preferredFragment)
                R.id.recommend->setCurrentFragment(recommendedFragment)
                R.id.favourite->setCurrentFragment(favouriteFragment)

            }
            true
        }



    }

    private val listener = object : OnFetchDataListener<ApiResponse> {
        override fun onFetchData(list: List<Headlines>, message: String) {
            showNews(list)
        }

        override fun onError(message: String?) {
            TODO("Not yet implemented")
        }

    }

    private fun setLayoutManager(){

    }

    private fun showNews(list: List<Headlines>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_main)
        val layoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        val adapter = CustomAdapter(this, list)
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.toolbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }
    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
}