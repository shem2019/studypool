package com.example.studypool.admin

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.studypool.R
import com.example.studypool.database.CourseDatabase
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class AdminReportsActivity : AppCompatActivity() {

    private lateinit var chartCourses: BarChart
    private lateinit var chartFiles: BarChart
    private lateinit var btnExportCsv: Button
    private lateinit var btnExportPdf: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_reports)

        chartCourses = findViewById(R.id.chartCourses)
        chartFiles = findViewById(R.id.chartFiles)
        btnExportCsv = findViewById(R.id.btnExportCSV)
        btnExportPdf = findViewById(R.id.btnExportPDF)

        lifecycleScope.launch {
            loadCharts()
        }

        btnExportCsv.setOnClickListener {
            // Stub: CSV export logic
        }

        btnExportPdf.setOnClickListener {
            // Stub: PDF export logic
        }
    }

    private suspend fun loadCharts() {
        val db = CourseDatabase.getDatabase(this)
        val allCourses = withContext(Dispatchers.IO) {
            db.courseDao().getAllCourses()
        }

        // 📈 Courses per Day (Demo logic — you could use week grouping instead)
        val uploadsByDay = allCourses.groupBy {
            it.timestamp.substringBefore(" ") ?: "Unknown"
        }.map { (date, list) ->
            BarEntry(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).dayOfMonth.toFloat(), list.size.toFloat())
        }

        val dataSet1 = BarDataSet(uploadsByDay, "Courses Uploaded")
        chartCourses.data = BarData(dataSet1)
        chartCourses.description = Description().apply { text = "Daily Course Uploads" }
        chartCourses.invalidate()

        // 📄 Files per Course
        val filesPerCourse = allCourses.map {
            val files: List<String> = Gson().fromJson(it.fileUris ?: "[]", object : TypeToken<List<String>>() {}.type)
            BarEntry(it.id.toFloat(), files.size.toFloat())
        }

        val dataSet2 = BarDataSet(filesPerCourse, "Files per Course")
        chartFiles.data = BarData(dataSet2)
        chartFiles.description = Description().apply { text = "Material Distribution" }
        chartFiles.invalidate()
    }
}
