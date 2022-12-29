package com.example.newsapplication.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.newsapplication.R
import com.google.firebase.auth.FirebaseAuth

class LogoutActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            auth.signOut()
        } else {
            Toast.makeText(this, "Already signed out", Toast.LENGTH_SHORT).show()
            val logoutText = findViewById<TextView>(R.id.success_text)
            logoutText.text = getString(R.string.already_logout)
        }
        val homeButton = findViewById<Button>(R.id.home_btn)
        val loginButton = findViewById<Button>(R.id.login_btn)
        homeButton.setOnClickListener {
            finish()
        }
        loginButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}