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
import com.example.newsapplication.R
import com.example.newsapplication.models.Headlines
import com.example.newsapplication.ui.activities.DetailsActivity
import com.example.newsapplication.ui.activities.SettingsActivity
import java.text.SimpleDateFormat
import java.util.*


class BroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(ContentValues.TAG, "Working")

        Log.i("Autostart", "started")



        when (intent?.action) {
            mBroadcastNotification -> {
                val headlines = intent?.getSerializableExtra("data") as Headlines
                sendNotification(headlines, context)
            }
        }


    }

    private fun sendNotification(headlines: Headlines?, context: Context){
        val title = headlines?.title
        val description = headlines?.description
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel("notificationID", "notification", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(context,NotificationManager::class.java)
            manager?.createNotificationChannel(channel)

        }
        val myIntent = Intent(context, DetailsActivity::class.java)
        myIntent.putExtra("data", headlines)

        val pendingIntent = PendingIntent.getActivity(context, 1, myIntent, PendingIntent.FLAG_IMMUTABLE)

        var builder = NotificationCompat.Builder(context, "notificationID")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        var managerCompat = NotificationManagerCompat.from(context)
        managerCompat.notify(1, builder.build())
    }
    companion object {
        const val mBroadcastNotification = "com.broadcast.notification"
        const val mBroadcastBoot = "android.intent.action.BOOT_COMPLETED"

    }
}