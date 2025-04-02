package com.example.studypool.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studypool.R
import com.example.studypool.database.CourseDatabase
import com.example.studypool.databinding.ActivityAdminLeaderboardBinding
import com.example.studypool.utils.XPManager
import kotlinx.coroutines.*
import androidx.lifecycle.lifecycleScope
import com.example.studypool.appctrl.LeaderboardItem

class AdminLeaderboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminLeaderboardBinding
    private lateinit var adapter: LeaderboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = LeaderboardAdapter()
        binding.leaderboardRecycler.layoutManager = LinearLayoutManager(this)
        binding.leaderboardRecycler.adapter = adapter

       // loadLeaderboardData()
        demoloadLeaderboardData()
    }

  /*  private fun loadLeaderboardData() {
        lifecycleScope.launch {
            val db = CourseDatabase.getDatabase(this@AdminLeaderboardActivity)
            val users = db.userDao().getAllUsers()

            val leaderboardItems = users.map {
                val xp = XPManager(this@AdminLeaderboardActivity).getXP(it.email)
                LeaderboardItem(name = it.email, xp = xp)
            }.sortedByDescending { it.xp }

            adapter.submitList(leaderboardItems)
        }*/

    private fun demoloadLeaderboardData() {
        // Inject some demo XP leaderboard items
        val demoLeaderboard = listOf(
            LeaderboardItem("alice@student.com", 920),
            LeaderboardItem("bob@student.com", 880),
            LeaderboardItem("carol@student.com", 790),
            LeaderboardItem("dave@student.com", 670),
            LeaderboardItem("eve@student.com", 610),
            LeaderboardItem("frank@student.com", 500),
            LeaderboardItem("alice@student.com", 920),
            LeaderboardItem("bob@student.com", 880),
            LeaderboardItem("carol@student.com", 790),
            LeaderboardItem("dave@student.com", 670),
            LeaderboardItem("eve@student.com", 610),
            LeaderboardItem("frank@student.com", 500),
            LeaderboardItem("grace@student.com", 480),
            LeaderboardItem("heidi@student.com", 460),
            LeaderboardItem("ivan@student.com", 440),
            LeaderboardItem("judy@student.com", 430),
            LeaderboardItem("ken@student.com", 400),
            LeaderboardItem("laura@student.com", 390),
            LeaderboardItem("mike@student.com", 380),
            LeaderboardItem("nancy@student.com", 360),
            LeaderboardItem("oliver@student.com", 340),
            LeaderboardItem("patty@student.com", 320),
            LeaderboardItem("quincy@student.com", 300),
            LeaderboardItem("rachel@student.com", 280),
            LeaderboardItem("steve@student.com", 260),
            LeaderboardItem("trudy@student.com", 250)
        )

        adapter.submitList(demoLeaderboard)
    }

}
