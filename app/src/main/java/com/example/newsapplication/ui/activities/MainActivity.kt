package com.example.newsapplication.ui.activities


import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.newsapplication.R
import com.example.newsapplication.ui.adapters.DayStreakCounter
import com.example.newsapplication.ui.fragments.SearchFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var entry: CharSequence
    private lateinit var email: String
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        setSupportActionBar(mToolbar)
        var drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        // Day Streak initialisation
        val dayStreakCounter = DayStreakCounter(this)
        dayStreakCounter.onUserLogin()
        val streak = dayStreakCounter.streak
        var streakText = "Streak: $streak"


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
        }
        else if (account != null){
            email = account.email.toString()
        }
        else{
            email = ""
            streakText = ""

        }


        // Set up drawer menu
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val headerView = navigationView.getHeaderView(0)
        val navUsername = headerView.findViewById<View>(R.id.profile_text) as TextView
        navUsername.text = email
        val navStreakCounter = headerView.findViewById<View>(R.id.day_streak) as TextView
        navStreakCounter.text = streakText
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
        navigationView.setupWithNavController(navController)
    }

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
        //displaySelectedScreen(item)
        return true
    }

    fun getQuery(): CharSequence {
        return entry
    }

    private fun getUsername(){
        val email = auth.currentUser?.email
    }


//    private fun displaySelectedScreen(item: MenuItem) {
//        val itemName = item.toString()
//        val profile = this.getString(R.string.menu_profile)
//
//        val logout = this.getString(R.string.menu_logout)
//        var intent: Intent?
//        when(itemName){
//            profile -> {
//                intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//
//            }
//
//            logout -> {
//                currentUser = auth.currentUser!!
//                if(currentUser != null){
//                    auth.signOut()
//                    Toast.makeText(this, "User signed out", Toast.LENGTH_SHORT).show()
//                }
//                else{
//                    Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        }
//
//    }


}