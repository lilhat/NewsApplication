package com.example.newsapplication.ui.activities


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.newsapplication.R
import com.example.newsapplication.services.NotificationService
import com.example.newsapplication.utils.MyWorker

import com.example.newsapplication.utils.DayStreakCounter
import com.example.newsapplication.ui.fragments.SearchFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit


open class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var entry: CharSequence
    private lateinit var email: String
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private var isLoggedIn: Boolean = false
    private lateinit var streakText: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        setSupportActionBar(mToolbar)
        var drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        // Set up drawer menu
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
        navigationView.setupWithNavController(navController)

        // Setup bottom nav menu
        bottomNavigationView.setupWithNavController(navController)

        // Call function to setup drawer text
        setupDrawerText()
    }

    // Whenever main activity is resumed, update drawer text
    override fun onResume() {
        super.onResume()
        setupDrawerText()
    }

    // Function to setup the drawer menu text
    private fun setupDrawerText(){
        // Day Streak initialisation
        val dayStreakCounter = DayStreakCounter(this)
        dayStreakCounter.onUserLogin()
        val streak = dayStreakCounter.streak
        streakText = "Day Streak: $streak"


        // Check if logged in
        db = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(currentUser != null){
            email = currentUser.email.toString()
            isLoggedIn = true
        }
        else if (account != null){
            email = account.email.toString()
            isLoggedIn = true
        }
        else{
            email = ""
            streakText = ""

        }
        val headerView = navigationView.getHeaderView(0)
        val navUsername = headerView.findViewById<View>(R.id.profile_text) as TextView
        navUsername.text = email
        val navStreakCounter = headerView.findViewById<View>(R.id.day_streak) as TextView
        navStreakCounter.text = streakText
        val navStreakReward = headerView.findViewById<View>(R.id.streak_reward) as ImageView

        // If day streak is above 6 show the reward badge on drawer header
        if(streak > 6){
            navStreakReward.visibility = View.VISIBLE
        }
        else {
            navStreakReward.visibility = View.INVISIBLE
        }
        hideItem()
    }


    // Whenever main activity is stopped, start notification worker
    override fun onStop() {
        super.onStop()
        if(SettingsActivity.isSet){
            Log.d(TAG, "Is Set")
            startServiceViaWorker()
        }
    }

    // Function to hide logout option from drawer menu if already logged out
    private fun hideItem() {
        navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val navMenu = navigationView.menu
        navMenu.findItem(R.id.logoutActivity).isVisible = isLoggedIn
    }

    // Inflating toolbar layout
    // Setting up search view to replace current fragment with search results fragment
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.toolbar_layout), menu)
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.queryHint = "Search keywords..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchFragment = SearchFragment()
                val manager = supportFragmentManager
                val transaction = manager.beginTransaction()
                entry = searchView.query
                val query = entry.toString()
                val bundle = Bundle()
                bundle.putString("message", query)
                searchFragment.arguments = bundle

                transaction.replace(R.id.flFragment, searchFragment)
                transaction.addToBackStack(null)
                transaction.commit()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    // Allow for toggling of the drawer menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item);
    }

    // Up navigation according to nav controller
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.newsNavHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }


    override fun onDestroy() {
        Log.d(TAG, "onDestroy called")
        super.onDestroy()
    }

    // Function to manually start foreground notification service
    @RequiresApi(Build.VERSION_CODES.O)
    fun startService() {
        Log.d(TAG, "startService called")
        if (!NotificationService.isServiceRunning) {
            val serviceIntent = Intent(this, NotificationService::class.java)
            startForegroundService(serviceIntent)
        }
    }

    // Function to manually stop notification service
    fun stopService() {
        Log.d(TAG, "stopService called")
        if (NotificationService.isServiceRunning) {
            val serviceIntent = Intent(this, NotificationService::class.java)
            stopService(serviceIntent)
        }
    }

    // Function to start service via worker, which allows for periodic requests to be made
    // Currently notifications are being sent every 16 minutes
    private fun startServiceViaWorker() {
        Log.d(TAG, "startServiceViaWorker called");
        val UNIQUE_WORK_NAME = "StartMyServiceViaWorker";
        val workManager = WorkManager.getInstance(this);

        val request = PeriodicWorkRequest.Builder(
            MyWorker::class.java,
            16,
            TimeUnit.MINUTES)
            .build();

        workManager.enqueueUniquePeriodicWork(UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request);

    }
}
    


