package com.example.newsapplication.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.content.BroadcastReceiver
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.newsapplication.R
import com.example.newsapplication.models.Headlines
import com.example.newsapplication.ui.activities.DetailsActivity
import java.util.*


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

