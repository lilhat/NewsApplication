package com.example.newsapplication.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.newsapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class ProfileActivity: AppCompatActivity() {
    private val db = Firebase.firestore
    private lateinit var profileName: TextView
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val loginButton = findViewById<Button>(R.id.login_btn)
        val registerButton = findViewById<Button>(R.id.register_btn)
        val submitButton = findViewById<Button>(R.id.submit_btn)
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        val orText = findViewById<TextView>(R.id.or_text)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        profileName = findViewById(R.id.profile_name)
        auth = FirebaseAuth.getInstance()
        loginButton?.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        registerButton?.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        submitButton?.setOnClickListener{
            finish()
        }
        val currentUser = auth.currentUser
        if(currentUser != null){
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show()
            profileName.text = auth.currentUser?.email
            loginButton.visibility = View.INVISIBLE
            registerButton.visibility = View.INVISIBLE
            orText.visibility = View.INVISIBLE

        }

    }


    override fun onResume() {
        super.onResume()
        profileName.text = auth.currentUser?.email
    }

}