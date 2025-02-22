package com.example.studypool

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studypool.R // Import R properly
import com.example.studypool.appctrl.Course

class CourseAdapter(
    private val courses: MutableList<Course>,
    private val onEditClick: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseName: TextView = itemView.findViewById(R.id.textCourseName)
        val editButton: ImageView = itemView.findViewById(R.id.btnEdit)

        fun bind(course: Course) {
            courseName.text = course.courseName
            editButton.setOnClickListener { onEditClick(course) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false) // Ensure item_course.xml exists
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(courses[position])
    }

    override fun getItemCount(): Int = courses.size
}
