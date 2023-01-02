package com.example.newsapplication.ui.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.newsapplication.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

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
    private val SHARED_PREF_NAME = "MyPref"
    private val KEY_BUSBOX = "Bus_Box"
    private val KEY_ENTBOX = "Ent_Box"
    private val KEY_ENVBOX = "Env_Box"
    private val KEY_FOOBOX = "Foo_Box"
    private val KEY_HEABOX = "Hea_Box"
    private val KEY_WORBOX = "Wor_Box"
    private val KEY_TOPBOX = "Top_Box"
    private val KEY_POLBOX = "Pol_Box"
    private val KEY_SPOBOX = "Spo_Box"
    private val KEY_SCIBOX = "Sci_Box"
    private val KEY_TECBOX = "Tec_Box"

    private val handler: Handler = Handler(Looper.getMainLooper())

    var categoryList: MutableList<String> = mutableListOf()

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

        submitButton?.setOnClickListener{
            getCategories()
            finish()
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


    fun getCategories(){
        for(checkBox in checkBoxes){
            addCategory(checkBox)
        }
        Toast.makeText(this, "Preferred categories: $categoryList", Toast.LENGTH_LONG).show()
    }

}