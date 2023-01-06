package com.example.newsapplication.services


import android.app.*
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.getIntent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.newsapplication.R
import com.example.newsapplication.models.Headlines
import com.example.newsapplication.ui.activities.DetailsActivity
import com.example.newsapplication.utils.BroadcastReceiver
import java.util.*

// Service to call notifications with provided headline
class NotificationService: Service() {

    init {
        Log.d(TAG, "constructor called")
        isServiceRunning = false
    }

    // Create notification channel when service first created
    // Setting variable to show service is running
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


    // Calling send notification function
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand called")
        sendNotification(intent)
        return START_STICKY

    }

    // When service is destroyed, set variable to false
    // Send broadcast to initiate service again
    override fun onDestroy() {
        Log.d(TAG, "onDestroy called")
        isServiceRunning = false
        stopForeground(true)
        val broadcastIntent = Intent(this, BroadcastReceiver::class.java)
        sendBroadcast(broadcastIntent)
        super.onDestroy()
    }

    // Function to create notification channel
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

    // Function to send notification with data from intent using foreground service
    private fun sendNotification(intent: Intent?){
        val headlines = intent?.getSerializableExtra("data") as Headlines
        val title = headlines.title
        val description = headlines.description
        val notificationIntent = Intent(this, DetailsActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        notificationIntent.putExtra("data", headlines)
        getIntent("").removeExtra("data")
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
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

        startForeground(1, notification)
    }

    companion object {
        private val TAG = "APIService"
        var isServiceRunning = false
        private val CHANNEL_ID = "NOTIFICATION_CHANNEL"
    }

}