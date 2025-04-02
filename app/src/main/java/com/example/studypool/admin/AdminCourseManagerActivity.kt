package com.example.studypool.admin

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studypool.R
import com.example.studypool.database.CourseDatabase
import com.example.studypool.database.CourseEntity
import com.google.gson.Gson
import kotlinx.coroutines.launch

class AdminCourseManagerActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private val db by lazy { CourseDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_course_manager)

        recycler = findViewById(R.id.recyclerAdminCourses)
        recycler.layoutManager = LinearLayoutManager(this)

        loadCourses()

        findViewById<Button>(R.id.btnLoadDemoCourses).setOnClickListener {
            insertDemoCourses()
        }

    }

    private fun loadCourses() {
        lifecycleScope.launch {
            val courses = db.courseDao().getAllCourses()
            recycler.adapter = AdminCourseAdapter(courses) { course ->
                lifecycleScope.launch {
                    db.courseDao().deleteCourse(course)
                    Toast.makeText(this@AdminCourseManagerActivity, "${course.courseName} deleted", Toast.LENGTH_SHORT).show()
                    loadCourses()
                }
            }
        }
    }
    private fun insertDemoCourses() {
        val courses = listOf(
            CourseEntity(
                id = 0,
                courseName = "Math 101",
                examDateTime = "2/4/2025 - 9:00",
                catDates = Gson().toJson(listOf("25/3/2025 - 11:00", "28/3/2025 - 14:00")),
                classTimes = Gson().toJson(listOf(Pair("Monday", "8:00"), Pair("Wednesday", "10:00"))),
                fileUris = Gson().toJson(listOf("content://math101_1.pdf", "content://math101_notes.pdf"))
            ),
            CourseEntity(
                id = 0,
                courseName = "History 201",
                examDateTime = "10/4/2025 - 11:30",
                catDates = Gson().toJson(listOf("1/4/2025 - 13:00")),
                classTimes = Gson().toJson(listOf(Pair("Tuesday", "9:00"))),
                fileUris = Gson().toJson(listOf("content://history201_ch1.pdf"))
            )
        )

        lifecycleScope.launch {
            db.courseDao().insertAllCourses(courses)
            Toast.makeText(this@AdminCourseManagerActivity, "Demo courses added", Toast.LENGTH_SHORT).show()
            loadCourses()
        }
    }

}
