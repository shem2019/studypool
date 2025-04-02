package com.example.studypool.admin

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.studypool.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class AdminInsightsActivity : AppCompatActivity() {

    private lateinit var activeUsersText: TextView
    private lateinit var topCourseText: TextView
    private lateinit var avgDurationText: TextView
    private lateinit var dropOffUsersText: TextView
    private lateinit var xpChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_insights)

        activeUsersText = findViewById(R.id.activeUsersText)
        topCourseText = findViewById(R.id.topCourseText)
        avgDurationText = findViewById(R.id.avgDurationText)
        dropOffUsersText = findViewById(R.id.dropOffUsersText)
        xpChart = findViewById(R.id.xpChart)

        // Navigate to Leaderboard when Active Users is clicked
        findViewById<LinearLayout>(R.id.activeuserslayout).setOnClickListener {
            startActivity(Intent(this, AdminLeaderboardActivity::class.java))
        }

        // Navigate to Course Manager when Top Course is clicked
        findViewById<LinearLayout>(R.id.topcourseslay).setOnClickListener {
            startActivity(Intent(this, AdminCourseManagerActivity::class.java))
        }

        loadInsights()
    }

    private fun loadInsights() {
        val sessions = DemoInsightsProvider.getDemoSessions()

        // 📌 Active Users
        val activeUsers = sessions.map { it.email }.distinct().size
        activeUsersText.text = activeUsers.toString()

        // 🏆 Top Course
        val topCourse = sessions.groupingBy { it.course }.eachCount()
            .entries.maxByOrNull { it.value }?.key ?: "—"
        topCourseText.text = topCourse

        // ⏱️ Avg Session Duration
        val avgDuration = sessions.map { it.sessionDurationMin }.average().toInt()
        avgDurationText.text = "$avgDuration min"

        // 📈 XP Growth (last 7 days)
        val groupedByDate = sessions.groupBy { it.date }
        val sortedDates = groupedByDate.keys.sorted()
        val chartEntries = sortedDates.mapIndexed { i, date ->
            Entry(i.toFloat(), groupedByDate[date]!!.sumOf { it.xpGained }.toFloat())
        }

        val dataSet = LineDataSet(chartEntries, "XP Gained")
        dataSet.setDrawFilled(true)
        dataSet.setDrawCircles(true)
        dataSet.lineWidth = 2f
        dataSet.valueTextSize = 10f

        val lineData = LineData(dataSet)
        xpChart.data = lineData

        val description = Description()
        description.text = "XP Growth (Last Days)"
        xpChart.description = description
        xpChart.invalidate()

        // ⚠️ Drop-Off Users (those with only 1 session total)
        val dropOffs = sessions.groupingBy { it.email }.eachCount()
            .filter { it.value == 1 }
            .keys
        dropOffUsersText.text = if (dropOffs.isNotEmpty()) {
            dropOffs.joinToString(", ")
        } else {
            "No drop-offs today"
        }
    }
}
