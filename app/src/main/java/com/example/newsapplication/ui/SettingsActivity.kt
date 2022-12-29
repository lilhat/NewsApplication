package com.example.newsapplication.ui

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.newsapplication.R

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
        // TODO - Send to firestore database
        submitButton?.setOnClickListener{
            getCategories()
            finish()
        }

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