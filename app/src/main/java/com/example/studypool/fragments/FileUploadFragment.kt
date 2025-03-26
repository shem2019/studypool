package com.example.studypool.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.studypool.R
import com.example.studypool.database.CourseDatabase
import com.example.studypool.database.CourseEntity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.*

class FileUploadFragment : Fragment() {
    private var selectedFileUris = mutableListOf<Uri>()
    private var selectedCatDates = mutableListOf<String>()
    private var selectedClassTimes = mutableListOf<Pair<String, String>>() // (Day, Time)
    private lateinit var database: CourseDatabase

    // File Picker
    private val pickFilesLauncher = registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
        if (uris.isNotEmpty()) {
            selectedFileUris.clear()
            selectedFileUris.addAll(uris)
            uris.forEach { persistFileAccess(it) }
            updateFileStatus()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_file_upload, container, false)
        database = CourseDatabase.getDatabase(requireContext())

        val btnUploadMaterial = view.findViewById<Button>(R.id.btnUploadMaterial)
        val btnPickExamDate = view.findViewById<Button>(R.id.btnPickExamDate)
        val btnAddCatDate = view.findViewById<Button>(R.id.btnAddCAT)
        val btnAddClassTime = view.findViewById<Button>(R.id.btnAddClassTime)
        val btnSave = view.findViewById<Button>(R.id.btnSaveCourse)
        val editTextCourseName = view.findViewById<EditText>(R.id.editTextCourseName)
        val txtExamDate = view.findViewById<TextView>(R.id.txtExamDate)
        val listCatDates = view.findViewById<ListView>(R.id.listCATDates)
        val listClassTimes = view.findViewById<ListView>(R.id.listClassTimes)

        btnUploadMaterial.setOnClickListener { pickFilesLauncher.launch(arrayOf("*/*")) }
        btnPickExamDate.setOnClickListener { pickDateTime { txtExamDate.text = it } }

        btnAddCatDate.setOnClickListener {
            pickDateTime { dateTime ->
                selectedCatDates.add(dateTime)
                updateListView(listCatDates, selectedCatDates)
            }
        }

        btnAddClassTime.setOnClickListener {
            pickDateTime { time ->
                val days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
                AlertDialog.Builder(requireContext())
                    .setTitle("Select Day")
                    .setItems(days) { _, which ->
                        selectedClassTimes.add(Pair(days[which], time))
                        updateListView(listClassTimes, selectedClassTimes.map { "${it.first}: ${it.second}" })
                    }.show()
            }
        }

        btnSave.setOnClickListener {
            val courseName = editTextCourseName.text.toString()
            if (courseName.isNotEmpty()) {
                val course = CourseEntity(
                    courseName = courseName,
                    fileUris = Gson().toJson(selectedFileUris.map { it.toString() }),
                    examDateTime = txtExamDate.text.toString(),
                    catDates = Gson().toJson(selectedCatDates),
                    classTimes = Gson().toJson(selectedClassTimes)
                )

                lifecycleScope.launch {
                    database.courseDao().insertCourse(course)
                    Toast.makeText(requireContext(), "Course Saved!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Enter Course Name!", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun pickDateTime(callback: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val formattedTime = String.format("%02d/%02d/%04d - %02d:%02d", dayOfMonth, month + 1, year, hour, minute)
                callback(formattedTime)
            }, 6, 0, true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }


    private fun persistFileAccess(uri: Uri) {
        val contentResolver = requireContext().contentResolver
        val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        contentResolver.takePersistableUriPermission(uri, takeFlags)
    }

    private fun updateListView(listView: ListView, items: List<String>) {
        listView.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
    }

    private fun updateFileStatus() {
        view?.findViewById<TextView>(R.id.txtFileStatus)?.text =
            "Selected ${selectedFileUris.size} files"
    }
}
