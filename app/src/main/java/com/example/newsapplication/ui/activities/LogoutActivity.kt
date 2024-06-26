package com.example.newsapplication.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.newsapplication.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

// Activity to logout user
class LogoutActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient

    // Calls logout function when activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        logOut()
        MainActivity.isLoggedIn = false
        val homeButton = findViewById<Button>(R.id.home_btn)
        val loginButton = findViewById<Button>(R.id.login_btn)

        // Setting home button to finish activity
        homeButton.setOnClickListener {
            finish()
        }
        // Setting login button to launch login activity
        loginButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // Function to logout either from firebase or from google
    private fun logOut() {
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            auth.signOut()
        } else {
            gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

            gsc = GoogleSignIn.getClient(this, gso)

            val account = GoogleSignIn.getLastSignedInAccount(this)
            if(account!=null){
                gsc.signOut().addOnCompleteListener{
                    Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Already signed out", Toast.LENGTH_SHORT).show()
                val logoutText = findViewById<TextView>(R.id.success_text)
                logoutText.text = getString(R.string.already_logout)
            }

        }
    }
}