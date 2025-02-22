package com.example.studypool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment


class profile : Fragment() {
    override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val hoursSeekBar = view.findViewById<SeekBar>(R.id.hoursSeekBar)
        val hoursTextView = view.findViewById<TextView>(R.id.hoursTextView)
        val switchInApp = view.findViewById<Switch>(R.id.switchInAppNotifications)
        val switchOutApp = view.findViewById<Switch>(R.id.switchOutAppNotifications)


        hoursSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                hoursTextView.text = "Selected Hours: $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Optional: Add any actions when the user starts dragging the slider
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Optional: Add any actions when the user stops dragging the slider
            }
        })

        switchInApp.setOnCheckedChangeListener { _, isChecked ->
            // Handle in-app notification switch change
            if (isChecked) {
                // Code to enable in-app notifications
            } else {
                // Code to disable in-app notifications
            }
        }

        switchOutApp.setOnCheckedChangeListener { _, isChecked ->
            // Handle out-of-app notification switch change
            if (isChecked) {
                // Code to enable out-of-app notifications
            } else {
                // Code to disable out-of-app notifications
            }
        }

    }
}
