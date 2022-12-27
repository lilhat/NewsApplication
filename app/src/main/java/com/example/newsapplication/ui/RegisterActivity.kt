package com.example.newsapplication.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.newsapplication.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = findViewById<EditText>(R.id.nameEntry)
        val email = findViewById<EditText>(R.id.emailEntry)
        val password = findViewById<EditText>(R.id.passwordEntry)
        val registerBtn = findViewById<Button>(R.id.register_btn)
        val db = Firebase.firestore
        registerBtn.setOnClickListener {
            val usernameTxt = username.text.toString()
            val emailTxt = email.text.toString()
            val passwordTxt = password.text.toString()
            if(usernameTxt.isEmpty() || emailTxt.isEmpty() || passwordTxt.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
            else{
                val user = hashMapOf(
                    "username" to usernameTxt,
                    "email" to emailTxt,
                    "password" to passwordTxt
                )
                db.collection("users").get()
                db.collection("users")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
                Toast.makeText(this, "User registered", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            setResult(RESULT_CANCELED)
            finish()
        }
        return true
    }
}