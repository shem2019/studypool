package com.example.studypool.admin

import android.app.AlertDialog
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.studypool.R
import com.example.studypool.database.CourseEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AdminCourseAdapter(
    private val courses: List<CourseEntity>,
    private val onDelete: (CourseEntity) -> Unit
) : RecyclerView.Adapter<AdminCourseAdapter.CourseViewHolder>() {

    class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.txtCourseName)
        val txtFiles: TextView = view.findViewById(R.id.txtFiles)
        val txtExam: TextView = view.findViewById(R.id.txtExam)
        val btnDelete: Button = view.findViewById(R.id.btnDeleteCourse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.txtName.text = "Course: ${course.courseName}"
        holder.txtExam.text = "Exam: ${course.examDateTime ?: "N/A"}"

        val fileCount = Gson().fromJson<List<String>>(course.fileUris, object : TypeToken<List<String>>() {}.type).size
        holder.txtFiles.text = "Files: $fileCount"

        holder.btnDelete.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete Course")
                .setMessage("Are you sure you want to delete ${course.courseName}?")
                .setPositiveButton("Delete") { _, _ -> onDelete(course) }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun getItemCount(): Int = courses.size
}
