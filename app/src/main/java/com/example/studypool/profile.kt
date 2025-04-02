package com.example.studypool

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

class profile : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etUni = view.findViewById<TextInputEditText>(R.id.etuni)
        val etYos = view.findViewById<TextInputEditText>(R.id.etyos)
        val etCourse = view.findViewById<TextInputEditText>(R.id.etcourse)
        val submitBtn = view.findViewById<Button>(R.id.pdsubmit)

        val hoursSeekBar = view.findViewById<SeekBar>(R.id.hoursSeekBar)
        val hoursTextView = view.findViewById<TextView>(R.id.hoursTextView)
        val switchInApp = view.findViewById<Switch>(R.id.switchInAppNotifications)
        val switchOutApp = view.findViewById<Switch>(R.id.switchOutAppNotifications)

        val prefs = requireContext().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)

        val displayCard = view.findViewById<View>(R.id.cardProfileDisplay)
        val displayUni = view.findViewById<TextView>(R.id.tvDisplayUni)
        val displayYos = view.findViewById<TextView>(R.id.tvDisplayYos)
        val displayCourse = view.findViewById<TextView>(R.id.tvDisplayCourse)
        val editBtn = view.findViewById<Button>(R.id.btnEditProfile)

        val editableCard = view.findViewById<View>(R.id.editablecard)

        // Helper to switch to view mode
        fun switchToDisplayMode() {
            val uni = etUni.text.toString()
            val yos = etYos.text.toString()
            val course = etCourse.text.toString()

            displayUni.text = "University: $uni"
            displayYos.text = "Year of Study: $yos"
            displayCourse.text = "Course: $course"

            editableCard.visibility = View.GONE
            displayCard.visibility = View.VISIBLE
        }

        // "Edit Profile" click shows editable form
        editBtn.setOnClickListener {
            editableCard.visibility = View.VISIBLE
            displayCard.visibility = View.GONE
        }

        // Restore saved profile if available
        val savedUni = prefs.getString("university", "")
        val savedYos = prefs.getString("yos", "")
        val savedCourse = prefs.getString("course", "")
        val savedHours = prefs.getInt("goal_hours", 12)

        if (!savedUni.isNullOrEmpty()) {
            etUni.setText(savedUni)
            etYos.setText(savedYos)
            etCourse.setText(savedCourse)
            hoursSeekBar.progress = savedHours
            hoursTextView.text = "Selected Hours: $savedHours"
            switchToDisplayMode()
        }

        // Submit btn
        submitBtn.setOnClickListener {
            val university = etUni.text.toString()
            val yos = etYos.text.toString()
            val course = etCourse.text.toString()
            val goalHours = hoursSeekBar.progress

            if (university.isBlank() || yos.isBlank() || course.isBlank()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            prefs.edit()
                .putString("university", university)
                .putString("yos", yos)
                .putString("course", course)
                .putInt("goal_hours", goalHours)
                .apply()

            Toast.makeText(requireContext(), "✅ Profile Submitted", Toast.LENGTH_SHORT).show()
            switchToDisplayMode()
        }

        // Update hours text
        hoursSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                hoursTextView.text = "Selected Hours: $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        switchInApp.setOnCheckedChangeListener { _, isChecked ->
            // Future: Handle in-app notifications toggle
        }

        switchOutApp.setOnCheckedChangeListener { _, isChecked ->
            // Future: Handle out-of-app notifications toggle
        }
    }
}
