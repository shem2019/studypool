package com.example.studypool.utils

import android.content.Context
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DailyGoalManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("DailyGoals", Context.MODE_PRIVATE)
    private val formatter = DateTimeFormatter.ISO_DATE

    private val today = LocalDate.now().format(formatter)

    fun resetIfNeeded() {
        val savedDate = prefs.getString("goal_date", null)
        if (savedDate != today) {
            prefs.edit()
                .putString("goal_date", today)
                .putBoolean("goal_study", false)
                .putBoolean("goal_cat", false)
                .putInt("goal_xp", 0)
                .apply()
        }
    }

    fun setGoalDone(key: String) {
        prefs.edit().putBoolean(key, true).apply()
    }

    fun isGoalDone(key: String): Boolean {
        return prefs.getBoolean(key, false)
    }

    fun getXp(): Int = prefs.getInt("goal_xp", 0)

    fun addXp(amount: Int) {
        prefs.edit().putInt("goal_xp", getXp() + amount).apply()
    }

    fun isAllComplete(): Boolean {
        return isGoalDone("goal_study") && isGoalDone("goal_cat")
    }
}
