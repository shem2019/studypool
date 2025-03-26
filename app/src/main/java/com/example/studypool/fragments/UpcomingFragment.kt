package com.example.studypool.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studypool.R
import com.example.studypool.adapters.UpcomingAdapter
import com.example.studypool.appctrl.UpcomingItem

class UpcomingFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_upcoming, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerUpcoming)

        val sampleList = listOf(
            UpcomingItem("class", "Monday Class", "25 Mar - 10:00", "Math"),
            UpcomingItem("cat", "CAT 1", "27 Mar - 09:00", "Biology"),
            UpcomingItem("exam", "Midterm", "30 Mar - 14:00", "Chemistry")
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = UpcomingAdapter(sampleList)

        return view
    }
}
