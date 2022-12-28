package com.example.newsapplication.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.newsapplication.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val email = findViewById<EditText>(R.id.emailEntry)
        val password = findViewById<EditText>(R.id.passwordEntry)
        val loginBtn = findViewById<Button>(R.id.login_btn)
        val registerBtn = findViewById<Button>(R.id.register_btn)
        val db = Firebase.firestore
        val auth = FirebaseAuth.getInstance()
        loginBtn.setOnClickListener {
            val emailTxt = email.text.toString()
            val passwordTxt = password.text.toString()
            if (emailTxt.isEmpty() || passwordTxt.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val user = hashMapOf(
                    "email" to emailTxt,
                    "password" to passwordTxt
                )
                var isEmpty = false;
                val col = db.collection("users").whereEqualTo("email", emailTxt)
                    .limit(1).get()
                    .addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                        if (task.isSuccessful) {
                            isEmpty = task.result.isEmpty
                        }
                    })
                auth.signInWithEmailAndPassword(emailTxt, passwordTxt)
                if (isEmpty) {
                    // TODO - Check if password matches
                    Toast.makeText(this, "User logged in", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
                }
            }
            registerBtn.setOnClickListener {
                startActivity(Intent(this, RegisterActivity::class.java))
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

