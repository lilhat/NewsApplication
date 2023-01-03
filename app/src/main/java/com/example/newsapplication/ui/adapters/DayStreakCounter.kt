package com.example.newsapplication.ui.adapters

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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
            // user logged in for the first time
            updateLastLoginDate(today)
            incrementDays()
            Toast.makeText(context, "First day", Toast.LENGTH_SHORT).show()
        } else {
            when (lastLoginDay) {
                today -> {
                    // User logged in the same day , do nothing
                    Toast.makeText(context, "Same day", Toast.LENGTH_SHORT).show()
                }
                yesterday -> {
                    // User logged in consecutive days , add 1
                    updateLastLoginDate(today)
                    incrementDays()
                    Toast.makeText(context, "Day Streak increased", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // It's been more than a day user logged in, reset the counter to 1
                    updateLastLoginDate(today)
                    resetDays()
                    Toast.makeText(context, "Day Streak reset", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getYesterdayDate(simpleDateFormat: DateFormat, date: Date): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, -1)
        return simpleDateFormat.format(calendar.time)
    }

    /**
     * Update last login date into the storage
     * @param date
     */
    private fun updateLastLoginDate(date: String) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putString("last_login_day", date)
        editor.apply();
    }


    /**
     * Get last login date
     * @return
     */
    private val lastLoginDate: String?
        private get() {
            sharedPreferences =
                context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString("last_login_day", null);
        }


    private val consecutiveDays: Int
        private get() {
            sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE)
            var days = sharedPreferences.getInt("num_consecutive_days", 0);
            return days;
        }

    private fun incrementDays() {
        val days = consecutiveDays + 1
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putInt("num_consecutive_days", days)
        editor.apply()
    }

    private fun resetDays() {
        val days = 1
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putInt("num_consecutive_days", days);
        editor.apply();
    }

    val streak: Int
        get() = consecutiveDays
}