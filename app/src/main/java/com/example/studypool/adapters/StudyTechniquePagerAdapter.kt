package com.example.studypool.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.studypool.techniques.PomodoroFragment
// (We'll add SpacedRepetitionFragment and FeynmanFragment later)

class StudyTechniquePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 1 // Add more later
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PomodoroFragment()
            else -> PomodoroFragment()
        }
    }
}
