package com.example.newsapplication.ui.activities

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.newsapplication.R
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        auth = Firebase.auth
        val username = findViewById<EditText>(R.id.nameEntry)
        val email = findViewById<EditText>(R.id.emailEntry)
        val password = findViewById<EditText>(R.id.passwordEntry)
        val registerBtn = findViewById<Button>(R.id.register_btn)
        val cancelBtn = findViewById<Button>(R.id.cancel_btn)
        val termsCheckBox = findViewById<CheckBox>(R.id.terms_checkbox)
        registerBtn.setOnClickListener {
            val usernameTxt = username.text.toString().trim()
            val emailTxt = email.text.toString().trim()
            val passwordTxt = password.text.toString().trim()
            val termsChecked = termsCheckBox.isChecked
            signUpUser(usernameTxt, emailTxt, passwordTxt, termsChecked)
        }
        cancelBtn.setOnClickListener{
            finish()
        }
    }



    private fun signUpUser(username: String, email: String, password: String, checked: Boolean) {
        val username = username
        val email = email
        val pass = password

        // check pass
        if (username.isBlank() || email.isBlank() || pass.isBlank()) {
            Toast.makeText(this, "Username, Email or Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }
        if (pass.length < 6) {
            Toast.makeText(this, "Password must be more than 6 characters", Toast.LENGTH_SHORT).show()
            return
        }
        if (!checked){
            Toast.makeText(this, "You must agree to the terms of service", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    saveUser(username, email)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "User already exists.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUser(username: String, email: String){
        val user = hashMapOf(
            "username" to username,
            "email" to email
        )
        var isEmpty = false;
        db.collection("users").whereEqualTo("username", username)
            .limit(1).get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                if (task.isSuccessful) {
                    isEmpty = task.result.isEmpty
                }
            })
        if (!isEmpty) {
            db.collection("users").get()
            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
            Toast.makeText(this, "User info saved", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
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