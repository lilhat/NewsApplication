package com.example.newsapplication.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.newsapplication.R
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Activity to authenticate login
class LoginActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private lateinit var googleLogin: ImageView
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var mCallBackManager: CallbackManager
    private lateinit var facebookLogin: LoginButton
    private val TAG = "FacebookAuthentication"
    private lateinit var authStateListener: AuthStateListener

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
        val cancelBtn = findViewById<Button>(R.id.cancel_btn)

        // Call function to setup social medial login
        setupSocialLogin()

        // Email and Password login
        registerBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        loginBtn.setOnClickListener{
            val emailTxt = email.text.toString().trim()
            val passwordTxt = password.text.toString().trim()
            loginUser(emailTxt, passwordTxt)
        }
        cancelBtn.setOnClickListener{
            finish()
        }
    }

    // Check if user is logged in already when activity is started
    // If logged in then finish the activity
    public override fun onStart() {
        super.onStart()
        auth.addAuthStateListener { auth }
        val currentUser = auth.currentUser
        if(currentUser != null){
            Toast.makeText(baseContext, "Already logged in", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // Function to setup both social medial login with buttons
    private fun setupSocialLogin(){
        // Google login
        googleLogin = findViewById(R.id.google_login)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)

        googleLogin.setOnClickListener{
            signInGoogle()
        }

        // Facebook login
        facebookLogin = findViewById(R.id.facebook_login_btn)
        facebookLogin.setPermissions("email", "public_profile")
        mCallBackManager = CallbackManager.Factory.create()

        facebookLogin.registerCallback(mCallBackManager, object: FacebookCallback<LoginResult> {
            override fun onCancel() {
                Log.d(TAG, "onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "onError$error")
            }

            override fun onSuccess(result: LoginResult) {
                Log.d(TAG, "onSuccess$result")
                handleFacebookToken(result.accessToken)
            }

        })
    }

    // Function to retrieve facebook login credential to sign in with firebase authentication
    private fun handleFacebookToken(accessToken: AccessToken) {
        Log.d(TAG, "handleFacebookToken$accessToken")
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                Log.d(TAG, "Facebook Success")
                finish()
            } else{
                Log.d(TAG, "Facebook Failed")
            }
        }
    }

    // Function to launch google sign in intent
    private fun signInGoogle() {
        val intent = gsc.signInIntent
        startActivityForResult(intent, 100)
    }

    // Function to retrieve result from sign in intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallBackManager.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                task.getResult(ApiException::class.java)
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: ApiException){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }

        }

    }

    // Function to login user with email and password
    private fun loginUser(email: String, password: String) {
        val email = email
        val pass = password
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(this, "User signed in", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "Email or password is invalid",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }


    // Setting up button to finish activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            setResult(RESULT_CANCELED)
            finish()
        }
        return true
    }
}

