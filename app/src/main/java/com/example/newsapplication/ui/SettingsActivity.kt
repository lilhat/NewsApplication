package com.example.newsapplication.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.newsapplication.R
import org.checkerframework.checker.units.qual.K

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
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences
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
        val submitButton = findViewById<Button>(R.id.submit_btn)
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,
            MODE_PRIVATE)
        editor = sharedPreferences.edit()

        setupCheckBox(busCheckBox, KEY_BUSBOX)
        setupCheckBox(entCheckBox, KEY_ENTBOX)
        setupCheckBox(envCheckBox, KEY_ENVBOX)
        setupCheckBox(fooCheckBox, KEY_FOOBOX)
        setupCheckBox(heaCheckBox, KEY_HEABOX)
        setupCheckBox(spoCheckBox, KEY_SPOBOX)
        setupCheckBox(polCheckBox, KEY_POLBOX)
        setupCheckBox(tecCheckBox, KEY_TECBOX)
        setupCheckBox(sciCheckBox, KEY_SCIBOX)
        setupCheckBox(worCheckBox, KEY_WORBOX)
        setupCheckBox(topCheckBox, KEY_TOPBOX)

        // TODO - Send to firestore database

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,
        MODE_PRIVATE)

        tickCheckBox(busCheckBox, KEY_BUSBOX)
        tickCheckBox(entCheckBox, KEY_ENTBOX)
        tickCheckBox(envCheckBox, KEY_ENVBOX)
        tickCheckBox(fooCheckBox, KEY_FOOBOX)
        tickCheckBox(heaCheckBox, KEY_HEABOX)
        tickCheckBox(spoCheckBox, KEY_SPOBOX)
        tickCheckBox(polCheckBox, KEY_POLBOX)
        tickCheckBox(tecCheckBox, KEY_TECBOX)
        tickCheckBox(sciCheckBox, KEY_SCIBOX)
        tickCheckBox(worCheckBox, KEY_WORBOX)
        tickCheckBox(topCheckBox, KEY_TOPBOX)



        submitButton?.setOnClickListener{
            getCategories()
            finish()
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
        addCategory(busCheckBox)
        addCategory(entCheckBox)
        addCategory(envCheckBox)
        addCategory(fooCheckBox)
        addCategory(tecCheckBox)
        addCategory(worCheckBox)
        addCategory(topCheckBox)
        addCategory(heaCheckBox)
        addCategory(spoCheckBox)
        addCategory(polCheckBox)
        addCategory(sciCheckBox)
        Toast.makeText(this, "Preferred categories: $categoryList", Toast.LENGTH_LONG).show()

    }

}