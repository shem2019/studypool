package com.example.studypool.admin

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.studypool.R
import com.example.studypool.database.CourseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var btnUsers: Button
    private lateinit var btnCourses: Button
    private lateinit var btnReports: Button
    private lateinit var btnLeaderboard: Button
    private lateinit var btnInsights: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        btnUsers = findViewById(R.id.btnUsers)
        btnCourses = findViewById(R.id.btnCourses)
        btnReports = findViewById(R.id.btnReports)
        btnLeaderboard = findViewById(R.id.btnLeaderboard)
        btnInsights = findViewById(R.id.btnInsights)
        val total_users=findViewById<TextView>(R.id.tvTotalUsers)
        val courses=findViewById<TextView>(R.id.tvTotalCourses)
        val uploadedfiles=findViewById<TextView>(R.id.tvTotalFiles)

        val db = CourseDatabase.getDatabase(this)

        lifecycleScope.launch {
            val userCount = withContext(Dispatchers.IO) {
                db.userDao().getAllUsers().size
            }

            val courseList = withContext(Dispatchers.IO) {
                db.courseDao().getAllCourses()
            }

            val fileCount = courseList.sumOf {
                Gson().fromJson<List<String>>(it.fileUris ?: "[]", object : TypeToken<List<String>>() {}.type).size
            }



            total_users.text="$userCount"
            courses.text="${courseList.size}"
            uploadedfiles.text="$fileCount"
        }

        findViewById<Button>(R.id.btnUsers).setOnClickListener {
            startActivity(Intent(this, AdminUserListActivity::class.java))
        }
        findViewById<Button>(R.id.btnCourses).setOnClickListener {
            startActivity(Intent(this, AdminCourseManagerActivity::class.java))
        }

        btnReports.setOnClickListener {
            startActivity(Intent(this, AdminReportsActivity::class.java))
        }

        btnLeaderboard.setOnClickListener {
            startActivity(Intent(this, AdminLeaderboardActivity::class.java))
        }

        btnInsights.setOnClickListener {
            startActivity(Intent(this, AdminInsightsActivity::class.java))

        }
    }
}
