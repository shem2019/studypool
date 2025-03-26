// ✅ Correct
package com.example.studypool.utils

import android.content.Context

class XPManager(context: Context) {
    private val prefs = context.getSharedPreferences("XP", Context.MODE_PRIVATE)

    fun getTotalXP(): Int = prefs.getInt("xp_total", 0)

    fun addXP(amount: Int) {
        val total = getTotalXP() + amount
        prefs.edit().putInt("xp_total", total).apply()
    }

    fun getLevel(): Int = getTotalXP() / 100
    fun getXPProgress(): Int = getTotalXP() % 100
    fun getXPToNextLevel(): Int = 100 - getXPProgress()
}
