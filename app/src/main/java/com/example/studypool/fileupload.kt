package com.example.studypool


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studypool.appctrl.Course
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar
class fileupload : Fragment() { private lateinit var recyclerView: RecyclerView
    private lateinit var courseAdapter: CourseAdapter
    private val courseList = mutableListOf<Course>()

    private val PICK_FILE_REQUEST = 1
    private var selectedFileUri: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_fileupload, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewCourses)
        val fab: FloatingActionButton = view.findViewById(R.id.fabAddCourse)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        courseAdapter = CourseAdapter(courseList) { course ->
            showAddCourseDialog(course)
        }
        recyclerView.adapter = courseAdapter

        fab.setOnClickListener {
            showAddCourseDialog(null)
        }

        return view
    }

    private fun showAddCourseDialog(existingCourse: Course?) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_course, null)
        val courseName = dialogView.findViewById<EditText>(R.id.editTextCourseName)
        val btnUploadMaterial = dialogView.findViewById<Button>(R.id.btnUploadMaterial)
        val txtFileStatus = dialogView.findViewById<TextView>(R.id.txtFileStatus)
        val btnPickExamDate = dialogView.findViewById<Button>(R.id.btnPickExamDate)
        val txtExamDate = dialogView.findViewById<TextView>(R.id.txtExamDate)
        val btnAddClassTime = dialogView.findViewById<Button>(R.id.btnAddClassTime)
        val listClassTimes = dialogView.findViewById<ListView>(R.id.listClassTimes)
        val btnAddCAT = dialogView.findViewById<Button>(R.id.btnAddCAT)
        val listCATs = dialogView.findViewById<ListView>(R.id.listCATDates)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSaveCourse)

        val classTimes = mutableListOf<Pair<String, String>>()
        val catDates = mutableListOf<String>()
        var examDate: String? = null

        // File Upload
        btnUploadMaterial.setOnClickListener {
            openFileManager()
        }

        // Pick Exam Date
        btnPickExamDate.setOnClickListener {
            pickDateTime { dateTime ->
                txtExamDate.text = dateTime
                examDate = dateTime
            }
        }

        // Add Class Time
        btnAddClassTime.setOnClickListener {
            pickDateTime { dateTime ->
                val days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
                AlertDialog.Builder(requireContext())
                    .setTitle("Select Day")
                    .setItems(days) { _, which ->
                        classTimes.add(Pair(days[which], dateTime))
                        listClassTimes.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, classTimes.map { "${it.first}: ${it.second}" })
                    }.show()
            }
        }

        // Add CAT Date
        btnAddCAT.setOnClickListener {
            pickDateTime { dateTime ->
                catDates.add(dateTime)
                listCATs.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, catDates)
            }
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .setPositiveButton("Save") { _, _ ->
                val newCourse = Course(
                    courseName = courseName.text.toString(),
                    courseMaterialUri = selectedFileUri,
                    classTimes = classTimes,
                    examDateTime = examDate,
                    catDateTimes = catDates
                )

                if (existingCourse == null) {
                    courseList.add(newCourse)
                } else {
                    val index = courseList.indexOf(existingCourse)
                    courseList[index] = newCourse
                }

                courseAdapter.notifyDataSetChanged()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    private fun pickDateTime(callback: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            TimePickerDialog(requireContext(), { _, hour, minute ->
                callback("$dayOfMonth/${month + 1}/$year - $hour:$minute")
            }, 6, 0, true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun openFileManager() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, PICK_FILE_REQUEST)
    }
}
