package com.example.newsapplication

import FavouriteFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var loadingPB: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        setSupportActionBar(mToolbar)
        val preferredFragment= PreferredFragment()
        val recommendedFragment=RecommendedFragment()
        val favouriteFragment=FavouriteFragment()

        loadingPB = findViewById(R.id.idPBLoading)
        loadingPB.visibility = View.VISIBLE
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.toolbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }
    private fun setCurrentFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
        loadingPB.visibility = View.GONE

    }


}