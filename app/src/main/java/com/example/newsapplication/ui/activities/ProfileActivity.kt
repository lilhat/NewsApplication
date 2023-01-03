package com.example.newsapplication.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.newsapplication.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileActivity: AppCompatActivity() {
    private val db = Firebase.firestore
    private lateinit var profileName: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String

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

        val currentUser = auth.currentUser
        if(currentUser != null){
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show()
            profileName.text = auth.currentUser?.email
            loginButton.visibility = View.INVISIBLE
            registerButton.visibility = View.INVISIBLE
            orText.visibility = View.INVISIBLE

        }
        else{
            val account = GoogleSignIn.getLastSignedInAccount(this)
            if(account!=null){
                email = account.email.toString()
                Toast.makeText(this, email, Toast.LENGTH_SHORT).show()
                profileName.text = email
                loginButton.visibility = View.INVISIBLE
                registerButton.visibility = View.INVISIBLE
                orText.visibility = View.INVISIBLE
            }
        }


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


    }


    override fun onResume() {
        super.onResume()
        val currentUser = auth.currentUser
        val account = GoogleSignIn.getLastSignedInAccount(this)
        val loginButton = findViewById<Button>(R.id.login_btn)
        val registerButton = findViewById<Button>(R.id.register_btn)
        val orText = findViewById<TextView>(R.id.or_text)
        if(currentUser != null){
            profileName.text = auth.currentUser?.email
            loginButton.visibility = View.INVISIBLE
            registerButton.visibility = View.INVISIBLE
            orText.visibility = View.INVISIBLE
        }
        else if(account!=null){
            email = account.email.toString()
            profileName.text = email
            loginButton.visibility = View.INVISIBLE
            registerButton.visibility = View.INVISIBLE
            orText.visibility = View.INVISIBLE
        }

    }

}