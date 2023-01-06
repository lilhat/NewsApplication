package com.example.newsapplication.ui.activities

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import com.example.newsapplication.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import java.util.*

// Activity to select categories that the user is interested in
class SettingsActivity: AppCompatActivity() {
    private lateinit var busCheckBox: CheckBox
    private lateinit var entCheckBox: CheckBox
    private lateinit var envCheckBox: CheckBox
    private lateinit var fooCheckBox: CheckBox
    private lateinit var tecCheckBox: CheckBox
    private lateinit var worCheckBox: CheckBox
    private lateinit var topCheckBox: CheckBox
    private lateinit var heaCheckBox: CheckBox
    private lateinit var spoCheckBox: CheckBox
    private lateinit var polCheckBox: CheckBox
    private lateinit var sciCheckBox: CheckBox
    private lateinit var checkBoxes: List<CheckBox>
    private lateinit var keyList: List<String>
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient

    // Setup views, if user is not logged in then preferences cannot be set
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val loggedText = findViewById<TextView>(R.id.logged_text)
        busCheckBox = findViewById(R.id.check_bus)
        entCheckBox = findViewById(R.id.check_ent)
        envCheckBox = findViewById(R.id.check_env)
        fooCheckBox = findViewById(R.id.check_foo)
        tecCheckBox = findViewById(R.id.check_tec)
        worCheckBox = findViewById(R.id.check_wor)
        topCheckBox = findViewById(R.id.check_top)
        heaCheckBox = findViewById(R.id.check_hea)
        spoCheckBox = findViewById(R.id.check_spo)
        polCheckBox = findViewById(R.id.check_pol)
        sciCheckBox = findViewById(R.id.check_sci)

        checkBoxes = mutableListOf(busCheckBox,entCheckBox,envCheckBox,fooCheckBox,tecCheckBox,
            worCheckBox,topCheckBox,heaCheckBox,spoCheckBox,polCheckBox,sciCheckBox)

        keyList = mutableListOf(KEY_BUSBOX,KEY_ENTBOX,KEY_ENVBOX,KEY_FOOBOX,KEY_TECBOX,
            KEY_WORBOX,KEY_TOPBOX,KEY_HEABOX,KEY_SPOBOX,KEY_POLBOX,KEY_SCIBOX)

        val notificationSwitch = findViewById<SwitchCompat>(R.id.set_notifications)
        val submitButton = findViewById<Button>(R.id.submit_btn)

        // Using shared preferences to store boolean values associated to category preference
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,
            MODE_PRIVATE)
        editor = sharedPreferences.edit()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if(currentUser == null) {
            if(account == null){
                hideCheckBoxes()
                loggedText.visibility = View.VISIBLE
                notificationSwitch.isClickable = false
                notificationSwitch.alpha = 0.5F
            }
            else{
                setupCheckBoxes()
                loggedText.visibility = View.INVISIBLE
            }
        }
        else{
            setupCheckBoxes()
            loggedText.visibility = View.INVISIBLE
        }
        getCategories()
        isSet = sharedPreferences.getBoolean(setKey, false)
        if(isSet){
            notificationSwitch.isChecked = true
        }
        submitButton?.setOnClickListener{
            getCategories()
            finish()
        }
        notificationSwitch?.setOnCheckedChangeListener { _, isChecked ->
            isSet = isChecked
            setNotifications()
        }

    }

    // Set isSet variable according to whether switch is set
    // Put boolean variable into shared preferences
    private fun setNotifications(){
        val notificationSwitch = findViewById<SwitchCompat>(R.id.set_notifications)
        if(!isSet){
            editor.putBoolean(setKey, isSet)
            editor.apply()
            notificationSwitch.isChecked = false
        }
        else{
            editor.putBoolean(setKey, isSet)
            editor.apply()
            notificationSwitch.isChecked = true
        }
    }

    // Function to loop through each checkbox and call setup and tick functions
    private fun setupCheckBoxes(){
        var i = 0
        for(checkBox in checkBoxes){
            val key = keyList[i]
            setupCheckBox(checkBox, key)
            i += 1
        }
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,
            MODE_PRIVATE)
        i = 0
        for(checkBox in checkBoxes){
            val key = keyList[i]
            tickCheckBox(checkBox, key)
            i += 1
        }
    }

    // Function to check whether checkbox is checked, then putting result into shared preferences
    private fun setupCheckBox(checkBox: CheckBox, key: String){
        checkBox.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
            var aBoolean = b
            editor.putBoolean(key, aBoolean)
            editor.apply()
        }
    }

    // Function to tick checkbox depending on previous shared preference input
    private fun tickCheckBox(checkBox: CheckBox, key: String){
        val isChecked = sharedPreferences.getBoolean(key, false)
        checkBox.isChecked = isChecked
    }

    // Function to loop through each checkbox and call hide function
    private fun hideCheckBoxes(){
        for(checkBox in checkBoxes){
            hideCheckBox(checkBox)
        }
    }

    // Function to hide checkbox when user is not logged in
    private fun hideCheckBox(checkBox: CheckBox){
        checkBox.alpha = 0.5F
        checkBox.isClickable = false

    }

    // Function to add category to list if associated checkbox is checked
    private fun addCategory(checkBox: CheckBox){
        val categoryName = checkBox.text.toString()
        if(checkBox.isChecked){
            val foundValue = categoryList.find { item -> item == categoryName }
            if(foundValue != null){
                return
            }else{
                categoryList.add(categoryName)

            }
        }
        else{
            categoryList.remove(categoryName)
        }
    }

    // Function to loop through all checkboxes and call add function
    private fun getCategories(){
        for(checkBox in checkBoxes){
            addCategory(checkBox)
        }
    }

    companion object{
        var isSet: Boolean = false
        var categoryList: MutableList<String> = mutableListOf()
        var setKey = "isSet"
        val SHARED_PREF_NAME = "MyPref"
        val KEY_BUSBOX = "Bus_Box"
        val KEY_ENTBOX = "Ent_Box"
        val KEY_ENVBOX = "Env_Box"
        val KEY_FOOBOX = "Foo_Box"
        val KEY_HEABOX = "Hea_Box"
        val KEY_WORBOX = "Wor_Box"
        val KEY_TOPBOX = "Top_Box"
        val KEY_POLBOX = "Pol_Box"
        val KEY_SPOBOX = "Spo_Box"
        val KEY_SCIBOX = "Sci_Box"
        val KEY_TECBOX = "Tec_Box"
    }


}