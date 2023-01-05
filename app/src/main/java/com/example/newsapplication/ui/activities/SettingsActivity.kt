package com.example.newsapplication.ui.activities

import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.newsapplication.R
import com.example.newsapplication.services.APIService
import com.example.newsapplication.services.BroadcastReceiver
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.collections.ArrayList

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

        val preferenceButton = findViewById<Button>(R.id.set_notifications)
        val submitButton = findViewById<Button>(R.id.submit_btn)


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
            preferenceButton.text = removeText
        }
        submitButton?.setOnClickListener{
            getCategories()
            finish()
        }
        preferenceButton?.setOnClickListener{
            setNotifications()
        }

    }

    override fun onResume() {
        super.onResume()
    }

    private fun setNotifications(){
        val preferenceButton = findViewById<Button>(R.id.set_notifications)
        if(!isSet){
            preferenceButton.text = removeText
            isSet = true
            editor.putBoolean(setKey, isSet)
            editor.apply()
        }
        else{
            preferenceButton.text = setText
            isSet = false
            editor.putBoolean(setKey, isSet)
            editor.apply()
        }
    }

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

    private fun setupCheckBox(checkBox: CheckBox, key: String){
        checkBox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            var aBoolean = b
            editor.putBoolean(key, aBoolean)
            editor.apply()
        })
    }

    private fun tickCheckBox(checkBox: CheckBox, key: String){
        val isChecked = sharedPreferences.getBoolean(key, false)
        checkBox.isChecked = isChecked
    }

    private fun hideCheckBoxes(){
        for(checkBox in checkBoxes){
            hideCheckBox(checkBox)
        }
    }

    private fun hideCheckBox(checkBox: CheckBox){
        checkBox.alpha = 0.5F
        checkBox.isClickable = false

    }

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


    private fun getCategories(){
        for(checkBox in checkBoxes){
            addCategory(checkBox)
        }
        Toast.makeText(this, "Preferred categories: $categoryList", Toast.LENGTH_LONG).show()
    }

    companion object{
        var isSet: Boolean = false
        var categoryList: MutableList<String> = mutableListOf()
        var preferenceList: MutableList<Int> = mutableListOf()
        val removeText = "Remove Notifications"
        val setText = "Set Notifications"
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