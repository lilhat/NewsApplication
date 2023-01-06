package com.example.newsapplication.ui.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.newsapplication.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


// Activity for account registration
class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
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

        // Calling setup social login function
        setupSocialLogin()

        // If register button is clicked, call signup function with data from fields
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

    // Checking on activity start whether user is already logged in
    override fun onStart() {
        super.onStart()

        auth.addAuthStateListener { auth }
        val currentUser = auth.currentUser
        if(currentUser != null){
            Toast.makeText(baseContext, "Already logged in", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // Function to setup Google and Facebook login with associated buttons
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

    // Function to create new user with email and password
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

    // Function to store username and email into separate Firestore database
    // This can be used in the future to store more information with the user account
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

    // Finish the activity when up button is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            setResult(RESULT_CANCELED)
            finish()
        }
        return true
    }
}