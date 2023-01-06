package com.example.newsapplication.utils

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

// Class to keep track of the user's streak of using the application
class DayStreakCounter(private val context: Context) {

    private val SHARED_PREF_NAME = "MyPref"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun onUserLogin() {
        val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = Date()
        val today: String = dateFormat.format(date)
        val lastLoginDay = lastLoginDate
        val yesterday = getYesterdayDate(dateFormat, date)
        if (lastLoginDay == null) {
            // First time login
            updateLastLoginDate(today)
            incrementDays()
        } else {
            when (lastLoginDay) {
                today -> {
                    // Same day login, do nothing
                }
                yesterday -> {
                    // Increment days if consecutive login
                    updateLastLoginDate(today)
                    incrementDays()
                }
                else -> {
                    // Reset streak if day is missed
                    updateLastLoginDate(today)
                    resetDays()
                }
            }
        }
    }

    // Function to get yesterdays date using calendar
    private fun getYesterdayDate(simpleDateFormat: DateFormat, date: Date): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, -1)
        return simpleDateFormat.format(calendar.time)
    }


    // Using shared preferences to store last login date
    private fun updateLastLoginDate(date: String) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putString("last_login_day", date)
        editor.apply();
    }


    // Using shared preferences to retrieve last login date
    private val lastLoginDate: String?
        private get() {
            sharedPreferences =
                context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("last_login_day", null)
        }


    // Using shared preferences to retrieve number of consecutive days
    private val consecutiveDays: Int
        private get() {
            sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt("num_consecutive_days", 0)
        }

    // Incrementing the consecutive days then storing this into shared preferences
    private fun incrementDays() {
        val days = consecutiveDays + 1
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putInt("num_consecutive_days", days)
        editor.apply()
        checkStreak()
    }

    // Resetting the days streak to 1
    private fun resetDays() {
        val days = 1
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putInt("num_consecutive_days", days)
        editor.apply()
    }

    // Checking if the streak of consecutive days is equal to 7, then displaying a message
    private fun checkStreak(){
        val days = sharedPreferences.getInt("num_consecutive_days", 0)
        if (days == 7){
            Toast.makeText(context, "Congratulations on your 7 day streak!", Toast.LENGTH_SHORT).show()
        }
    }

    val streak: Int
        get() = consecutiveDays
}