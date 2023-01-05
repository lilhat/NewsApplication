package com.example.newsapplication.services


import android.app.*
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.getIntent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.newsapplication.R
import com.example.newsapplication.models.Headlines
import com.example.newsapplication.ui.activities.DetailsActivity
import java.util.*


class APIService: Service() {
//    var timer: Timer? = null
//    var timerTask: TimerTask? = null
//    var TAG = "Timers"
//    var Your_X_SECS = 5

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
//        startTimer(intent)
        sendNotification(intent)
        return START_STICKY

    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy called")
//        stopTimerTask()
        isServiceRunning = false
        stopForeground(true)

        val broadcastIntent = Intent(this, BroadcastReceiver::class.java)
        sendBroadcast(broadcastIntent)

        super.onDestroy()
    }

//    val handler: Handler = Handler()
//
//
//    fun startTimer(intent: Intent?) {
//        //set a new Timer
//        timer = Timer()
//
//        //initialize the TimerTask's job
//        initializeTimerTask(intent)
//
//        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
//        timer!!.schedule(timerTask, 5000, (Your_X_SECS * 1000).toLong()) //
//        //timer.schedule(timerTask, 5000,1000); //
//    }
//
//    fun stopTimerTask() {
//        //stop the timer, if it's not already null
//        if (timer != null) {
//            timer!!.cancel()
//            timer = null
//        }
//    }
//
//    fun initializeTimerTask(intent: Intent?) {
//        timerTask = object : TimerTask() {
//            override fun run() {
//
//                handler.post {
//                    sendNotification(intent)
//                }
//            }
//        }
//    }

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