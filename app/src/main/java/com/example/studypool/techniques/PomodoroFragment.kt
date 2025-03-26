package com.example.studypool.techniques

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studypool.CourseAdapter
import com.example.studypool.FileAdapter
import com.example.studypool.InAppPdfViewerActivity
import com.example.studypool.R
import com.example.studypool.database.CourseDatabase
import com.example.studypool.utils.XPManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class PomodoroFragment : Fragment() {

    private lateinit var timer: CountDownTimer
    private var running = false
    private var timeLeft = 25 * 60 * 1000L // 25 mins

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_pomodoro, container, false)

        val timerText = view.findViewById<TextView>(R.id.timerText)
        val startBtn = view.findViewById<Button>(R.id.startBtn)
        val resetBtn = view.findViewById<Button>(R.id.resetBtn)
        val guideText = view.findViewById<TextView>(R.id.guideText)

        guideText.text = "🍅 Pomodoro helps you focus in 25-min sprints with short breaks in between. Try to do one task fully!"

        val db = CourseDatabase.getDatabase(requireContext())

        val courseSpinner = view.findViewById<Spinner>(R.id.courseSpinner)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewFiles)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        lifecycleScope.launch {
            val allCourses = db.courseDao().getAllCourses()
            val courseNames = allCourses.map { it.courseName }

            // Set up the spinner
            val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, courseNames)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            courseSpinner.adapter = spinnerAdapter

            courseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedCourse = courseNames[position]
                    val selectedEntity = allCourses.find { it.courseName == selectedCourse }

                    selectedEntity?.let { course ->
                        val filteredList = listOf(course)

                        val adapter = CourseAdapter(
                            context = requireContext(),
                            courses = filteredList,
                            onDelete = {}, // no delete in pomodoro

                        )

                        recyclerView.adapter = adapter
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }




        lifecycleScope.launch {
            val allCourses = db.courseDao().getAllCourses()
            val fileUris = allCourses.flatMap { course ->
                val files: List<String> = Gson().fromJson(course.fileUris, object : TypeToken<List<String>>() {}.type)
                files
            }

            recyclerView.adapter = FileAdapter(requireContext(), fileUris) { fileUri ->
                val intent = Intent(requireContext(), InAppPdfViewerActivity::class.java)
                intent.putExtra("fileUri", fileUri)
                startActivity(intent)
            }




        }


        fun updateTimerText() {
            val minutes = (timeLeft / 1000) / 60
            val seconds = (timeLeft / 1000) % 60
            timerText.text = String.format("%02d:%02d", minutes, seconds)
        }

        startBtn.setOnClickListener {
            if (!running) {
                running = true
                timer = object : CountDownTimer(timeLeft, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        timeLeft = millisUntilFinished
                        updateTimerText()
                    }

                    override fun onFinish() {
                        running = false
                        Toast.makeText(requireContext(), "Pomodoro session complete! Take a break 🍵", Toast.LENGTH_LONG).show()
                    }
                }.start()
            }
        }

        resetBtn.setOnClickListener {
            timer.cancel()
            timeLeft = 25 * 60 * 1000L
            running = false
            updateTimerText()
        }

        updateTimerText()
        return view
    }
    private fun openPDF(fileUri: String) {
        try {
            val uri = Uri.parse(fileUri)

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "*/*") // accept all types to trigger Drive
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(intent, "Open with"))


            startActivity(Intent.createChooser(intent, "Open with"))

            // XP reward
            XPManager(requireContext()).addXP(5)
            Log.d("XP_DEBUG", "XP +5 for opening file: $fileUri")

        } catch (e: Exception) {
            Log.e("PDF_ERROR", "Failed to open file: ${e.message}")
            Toast.makeText(requireContext(), "No compatible app found to open this file", Toast.LENGTH_LONG).show()
        }
    }

    private fun getMimeType(uri: Uri): String {
        val contentResolver = requireContext().contentResolver
        return contentResolver.getType(uri) ?: "application/pdf" // fallback to PDF
    }



}
