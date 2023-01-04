package com.example.newsapplication.services


import android.app.*
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.newsapplication.R
import com.example.newsapplication.models.ApiResponse
import com.example.newsapplication.models.Headlines
import com.example.newsapplication.ui.activities.DetailsActivity
import com.example.newsapplication.ui.activities.MainActivity
import com.example.newsapplication.ui.adapters.OnFetchDataListener
import java.text.SimpleDateFormat
import java.util.*


class APIService: Service() {


    init {
        Log.d(TAG, "constructor called")
        isServiceRunning = false
    }


    override fun onCreate() {
        super.onCreate()
        super.onCreate()
        Log.d(TAG, "onCreate called")
        createNotificationChannel()
        isServiceRunning = true

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand called")
        val headlines = intent?.getSerializableExtra("data") as Headlines
        val title = headlines.title
        val description = headlines.description
        val notificationIntent = Intent(this, DetailsActivity::class.java)
        notificationIntent.putExtra("data", headlines)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, PendingIntent.FLAG_MUTABLE
        )
        Log.d(TAG, "notification called")
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        var managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(1, notification)
//        startForeground(1, notification)

        return START_STICKY

    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy called")
        isServiceRunning = false
        stopForeground(true)

        val broadcastIntent = Intent(this, BroadcastReceiver::class.java)
        sendBroadcast(broadcastIntent)

        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val appName = getString(R.string.app_name)
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                appName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }



//    private fun sendNotification(headlines: Headlines?){
//        val title = headlines?.title
//        val description = headlines?.description
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            val channel = NotificationChannel("notificationID", "notification", NotificationManager.IMPORTANCE_DEFAULT)
//            val manager = getSystemService(NotificationManager::class.java)
//            manager.createNotificationChannel(channel)
//
//        }
//        val myIntent = Intent(this, DetailsActivity::class.java)
//        myIntent.putExtra("data", headlines)
//
//        val pendingIntent = PendingIntent.getActivity(this, 1, myIntent, PendingIntent.FLAG_IMMUTABLE)
//
//        var builder = NotificationCompat.Builder(this, "notificationID")
//            .setSmallIcon(R.drawable.logo)
//            .setContentTitle(title)
//            .setContentText(description)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//        var managerCompat = NotificationManagerCompat.from(this)
//        managerCompat.notify(1, builder.build())
//    }

    companion object {
        private val TAG = "APIService"
        var isServiceRunning = false
        private val CHANNEL_ID = "NOTIFICATION_CHANNEL"
    }

}