package com.example.newsapplication.utils

import android.content.*
import android.content.BroadcastReceiver
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

// Class to receive broadcasts when service is destroyed, in order to restart the service
class BroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        Log.d(ContentValues.TAG, "Working")

        Log.i("Autostart", "started")


        val workManager = WorkManager.getInstance(context)
        val startServiceRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .build()
        workManager.enqueue(startServiceRequest)
        }


    }

