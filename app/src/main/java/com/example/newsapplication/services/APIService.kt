package com.example.newsapplication.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.newsapplication.models.ApiResponse
import com.example.newsapplication.models.Headlines
import com.example.newsapplication.R
import com.example.newsapplication.ui.activities.DetailsActivity
import com.example.newsapplication.ui.activities.SettingsActivity
import com.example.newsapplication.ui.adapters.OnFetchDataListener
import com.example.newsapplication.ui.adapters.RequestManager
import java.text.SimpleDateFormat
import java.util.*

class APIService: Service() {

    private lateinit var currentTime: Date
    private var listHeadlines: MutableList<Headlines>? = null
    private var item: Headlines? = null
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(ContentValues.TAG, "Service started")

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val settingsActivity = SettingsActivity()
        currentTime = Calendar.getInstance().time
        val categoryList = intent?.getStringArrayListExtra("data")
        if (categoryList != null) {
            for(category in categoryList) {
                Log.i(ContentValues.TAG, "In onStartCommand")
                val requestManager = RequestManager(this)
                requestManager.getNewsHeadlines(listener, null, category, null)
                Thread(Runnable {
                    try {
                        Thread.sleep(5000)
                        val broadcastIntent = Intent()
                        broadcastIntent.action = BroadcastReceiver.mBroadcastNotification
                        broadcastIntent.putExtra("data", item)
                        sendBroadcast(broadcastIntent)
                        Thread.sleep(5000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }


                }).start()
            }

        }

//        Thread(Runnable {
//            try {
//                Thread.sleep(5000)
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//            }
//
//            val broadcastIntent = Intent()
//            broadcastIntent.action = mBroadcastStringAction
//            broadcastIntent.putExtra("Data", getResources().getString(R.string.data))
//            sendBroadcast(broadcastIntent)
//
//            try {
//                Thread.sleep(5000)
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//            }
//
//            broadcastIntent.action = MainActivity.mBroadcastIntegerAction
//            broadcastIntent.putExtra("Data", 10)
//            sendBroadcast(broadcastIntent)
//
//            try {
//                Thread.sleep(5000)
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//            }
//
//            broadcastIntent.action = MainActivity.mBroadcastArrayListAction
//            broadcastIntent.putExtra("Data", mList)
//            sendBroadcast(broadcastIntent)
//        }).start()
        return START_STICKY

    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show()
    }

    private val listener = object :
        OnFetchDataListener<ApiResponse> {
        override fun onFetchData(list: MutableList<Headlines>?, message: String?) {
            firstItem(list)
            Thread.sleep(5000)
        }

        override fun onError(message: String?) {

        }

    }

    private fun firstItem(list: MutableList<Headlines>?) {
        listHeadlines = list
        val firstItem = list?.get(0)
        val dateTime = firstItem?.pubDate
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentFormatted = formatter.format(currentTime)
        val finalTime = formatter.parse(currentFormatted)
        val date = formatter.parse(dateTime)
        val cmp = date.compareTo(finalTime)
        when {
            cmp > 0 -> {
                Log.d(ContentValues.TAG, "$date is after $finalTime")

            }
            cmp < 0 -> {
                Log.d(ContentValues.TAG, "$date is before $finalTime")
                item = firstItem
            }
            else -> {
                Log.d(ContentValues.TAG, "Both Equal")
            }
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



}