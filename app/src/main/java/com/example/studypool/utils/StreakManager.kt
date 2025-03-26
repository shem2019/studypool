package com.example.studypool.utils

import android.content.Context
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StreakManager(context: Context) {
    private val prefs = context.getSharedPreferences("StudyPrefs", Context.MODE_PRIVATE)
    private val dateFormatter = DateTimeFormatter.ISO_DATE

    data class StreakResult(val count: Int, val milestoneMessage: String?, val xpReward: Int,val brokeStreak: Boolean)

    fun updateStreak(): StreakResult {
        val today = LocalDate.now()
        val lastDateString = prefs.getString("last_study_date", null)
        val lastDate = lastDateString?.let { LocalDate.parse(it, dateFormatter) }
        val currentStreak = prefs.getInt("study_streak", 0)

        val brokeStreak = lastDate != null && lastDate.isBefore(today.minusDays(1))

        val newStreak = when {
            lastDate == null || lastDate.plusDays(1) == today -> currentStreak + 1
            lastDate == today -> currentStreak
            else -> 1
        }

        prefs.edit()
            .putString("last_study_date", today.format(dateFormatter))
            .putInt("study_streak", newStreak)
            .apply()

        val message = when (newStreak) {
            3 -> "🔥 Triple Threat! 3-day streak!"
            5 -> "💪 You're on fire! 5-day streak!"
            7 -> "🏆 Streak Master! 7 days!"
            else -> null
        }

        val xp = when (newStreak) {
            3 -> 10
            5 -> 25
            7 -> 50
            else -> 0
        }

        return StreakResult(
            count = newStreak,
            milestoneMessage = message,
            xpReward = xp,
            brokeStreak = brokeStreak
        )
    }



    fun getStreak(): Int {
        return prefs.getInt("study_streak", 0)
    }
}
