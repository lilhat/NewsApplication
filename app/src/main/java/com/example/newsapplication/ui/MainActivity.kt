package com.example.newsapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.newsapplication.R
import com.example.newsapplication.ui.fragments.FavouriteFragment
import com.example.newsapplication.ui.fragments.PreferredFragment
import com.example.newsapplication.ui.fragments.RecommendedFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        setSupportActionBar(mToolbar)
        var drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        val preferredFragment= PreferredFragment()
        val recommendedFragment= RecommendedFragment()
        val favouriteFragment= FavouriteFragment()
        val firebaseAuth = FirebaseAuth.getInstance();

        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)





    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.toolbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.newsNavHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        displaySelectedScreen(item);
        //make this method blank
        return true;
    }

    private fun displaySelectedScreen(item: MenuItem) {
        val itemName = item.toString()
        val profile = this.getString(R.string.menu_profile)
        val settings = this.getString(R.string.menu_settings)
        val logout = this.getString(R.string.menu_logout)
        var intent: Intent?
        when(itemName){
            profile -> {
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            settings -> {
                intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
            logout -> {
                intent = Intent(this, DetailsActivity::class.java)
                startActivity(intent)
            }
        }

    }


}