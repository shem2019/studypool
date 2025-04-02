package com.example.studypool.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studypool.R
import com.example.studypool.database.CourseDatabase
import com.example.studypool.database.CourseEntity
import kotlinx.coroutines.*

class AdminCoursesActivity : AppCompatActivity(), CoroutineScope {
    private val job = Job()
    override val coroutineContext = Dispatchers.Main + job

    private lateinit var recycler: RecyclerView
    private lateinit var db: CourseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_courses)

        recycler = findViewById(R.id.recyclerAdminCourses)
        recycler.layoutManager = LinearLayoutManager(this)
        db = CourseDatabase.getDatabase(this)

        loadCourses()
    }

    private fun loadCourses() {
        launch {
            val courseList = withContext(Dispatchers.IO) {
                db.courseDao().getAllCourses()
            }

            recycler.adapter = AdminCourseAdapter(courseList) { course ->
                deleteCourse(course)
            }
        }
    }

    private fun deleteCourse(course: CourseEntity) {
        launch {
            withContext(Dispatchers.IO) {
                db.courseDao().deleteCourse(course)
            }
            Toast.makeText(this@AdminCoursesActivity, "Deleted ${course.courseName}", Toast.LENGTH_SHORT).show()
            loadCourses()
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}
