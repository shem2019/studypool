package com.example.studypool

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.studypool.adapters.StudyTechniquePagerAdapter

class StudyTechniquesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_techniques)

        val viewPager = findViewById<ViewPager2>(R.id.techniquesViewPager)
        viewPager.adapter = StudyTechniquePagerAdapter(this)
    }
}
