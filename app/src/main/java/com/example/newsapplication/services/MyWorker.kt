package com.example.newsapplication.services

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startForegroundService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.newsapplication.models.ApiResponse
import com.example.newsapplication.models.Headlines
import com.example.newsapplication.ui.activities.SettingsActivity
import com.example.newsapplication.interfaces.OnFetchDataListener
import com.example.newsapplication.utils.RequestManager


class MyWorker(
    context: Context,
    params: WorkerParameters?
) : Worker(context, params!!) {
    private val context: Context
    private val TAG = "MyWorker"
    private lateinit var sharedPreferences: SharedPreferences
    private var categoryList: MutableList<String> = mutableListOf()

    init {
        this.context = context
    }

    override fun doWork(): Result {
        if(SettingsActivity.isSet){
            sharedPreferences = context.getSharedPreferences(SettingsActivity.SHARED_PREF_NAME,
                AppCompatActivity.MODE_PRIVATE
            )
            getCategories()
            if(categoryList.isNotEmpty()){
                val random = (0 until categoryList.size).random()
                Log.i(ContentValues.TAG, "In doWork")
                val requestManager = RequestManager(context)
                requestManager.getNewsHeadlines(listener, null, categoryList[random], null)
                // TODO - replace random with an incrementing integer
            }
        }
        return Result.success()
    }

    fun getCategories(){
        addCategories("business", SettingsActivity.KEY_BUSBOX)
        addCategories("entertainment", SettingsActivity.KEY_ENTBOX)
        addCategories("environment", SettingsActivity.KEY_ENVBOX)
        addCategories("food", SettingsActivity.KEY_FOOBOX)
        addCategories("health", SettingsActivity.KEY_HEABOX)
        addCategories("world", SettingsActivity.KEY_WORBOX)
        addCategories("top", SettingsActivity.KEY_TOPBOX)
        addCategories("politics", SettingsActivity.KEY_POLBOX)
        addCategories("sports", SettingsActivity.KEY_SPOBOX)
        addCategories("science", SettingsActivity.KEY_SCIBOX)
        addCategories("technology", SettingsActivity.KEY_TECBOX)

    }

    fun addCategories(string: String, key: String){
        sharedPreferences = context.getSharedPreferences(SettingsActivity.SHARED_PREF_NAME,
            AppCompatActivity.MODE_PRIVATE
        )
        val bool = sharedPreferences.getBoolean(key, false)
        if(bool){
            categoryList.add(string)
        }
    }

    override fun onStopped() {
        Log.d(TAG, "onStopped called for: " + this.id)
        super.onStopped()
    }

    private val listener = object :
        OnFetchDataListener<ApiResponse> {
        override fun onFetchData(list: MutableList<Headlines>?, message: String?) {
            firstItem(list)
        }

        override fun onError(message: String?) {

        }

    }


    private fun firstItem(list: MutableList<Headlines>?) {
        val firstItem = list?.get(0)
        Log.d(TAG, "doWork called for: " + this.id)
        Log.d(TAG, "Service Running: " + APIService.isServiceRunning)
        Log.d(TAG, "starting service from doWork")
        val intent = Intent(context, APIService::class.java)
        intent.putExtra("data", firstItem)
        startForegroundService(context, intent)

    }

}