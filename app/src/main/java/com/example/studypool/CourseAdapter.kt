package com.example.studypool

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studypool.database.CourseEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CourseAdapter(
    private val context: Context,
    private val courses: List<CourseEntity>,
    private val onDelete: (CourseEntity) -> Unit,
   // private val onFileClick: ((String) -> Unit)? = null
) : RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val courseName: TextView = view.findViewById(R.id.courseName)
        val examDate: TextView = view.findViewById(R.id.examDate)
        val catDates: TextView = view.findViewById(R.id.catDates)
        val classTimes: TextView = view.findViewById(R.id.classTimes)
        val recyclerViewFiles: RecyclerView = view.findViewById(R.id.recyclerViewFiles)
        val deleteCourse: ImageView = view.findViewById(R.id.deleteCourse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = courses[position]
        holder.courseName.text = course.courseName
        holder.examDate.text = "Exam: ${course.examDateTime ?: "Not Set"}"

        val catDatesJson = course.catDates
        val catDates: List<String> = if (!catDatesJson.isNullOrEmpty() && catDatesJson != "null") {
            Gson().fromJson(catDatesJson, object : TypeToken<List<String>>() {}.type)
        } else {
            emptyList()
        }
        holder.catDates.text = "CATs: ${catDates.joinToString(", ")}"

        val classTimesJson = course.classTimes
        val classTimes: List<Pair<String, String>> = if (!classTimesJson.isNullOrEmpty() && classTimesJson != "null") {
            Gson().fromJson(classTimesJson, object : TypeToken<List<Pair<String, String>>>() {}.type)
        } else {
            emptyList()
        }
        holder.classTimes.text = "Classes: ${classTimes.joinToString(", ") { "${it.first} - ${it.second}" }}"

        val fileUrisJson = course.fileUris
        val fileUris: List<String> = if (!fileUrisJson.isNullOrEmpty() && fileUrisJson != "null") {
            Gson().fromJson(fileUrisJson, object : TypeToken<List<String>>() {}.type)
        } else {
            emptyList()
        }

        holder.recyclerViewFiles.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.recyclerViewFiles.adapter = FileAdapter(context, fileUris) { fileUri ->
            val intent = Intent(context, InAppPdfViewerActivity::class.java).apply {
                putExtra("fileUri", fileUri)
                putExtra("fileName", fileUri.substringAfterLast("/"))
                Log.d("we got to course adapter",fileUri)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Important for launching from adapter context
            }
            context.startActivity(intent)
        }

        // Handle delete
        holder.deleteCourse.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete Course")
                .setMessage("Are you sure you want to delete '${course.courseName}'?")
                .setPositiveButton("Delete") { _, _ -> onDelete(course) }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun getItemCount(): Int = courses.size
}
