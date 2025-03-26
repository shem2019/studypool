package com.example.studypool.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studypool.CourseAdapter
import com.example.studypool.R
import com.example.studypool.database.CourseDatabase
import com.example.studypool.database.CourseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudyFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CourseAdapter
    private lateinit var database: CourseDatabase
    private var courseList = mutableListOf<CourseEntity>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_study, container, false)
        database = CourseDatabase.getDatabase(requireContext())

        recyclerView = view.findViewById(R.id.recyclerViewCourses)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadCourses()

        return view
    }

    private fun loadCourses() {
        lifecycleScope.launch {
            courseList = database.courseDao().getAllCourses().toMutableList()
            adapter = CourseAdapter(requireContext(), courseList) { courseToDelete ->
                deleteCourse(courseToDelete)
            }
            recyclerView.adapter = adapter
        }
    }

    private fun deleteCourse(course: CourseEntity) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                database.courseDao().deleteCourse(course)
            }
            courseList.remove(course)
            adapter.notifyDataSetChanged()
        }
    }
}
