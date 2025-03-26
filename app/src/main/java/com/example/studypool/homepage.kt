package com.example.studypool

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studypool.adapters.UpcomingAdapter
import com.example.studypool.appctrl.UpcomingItem
import com.example.studypool.database.CourseDatabase
import com.example.studypool.utils.DailyGoalManager
import com.example.studypool.utils.StreakManager
import com.example.studypool.utils.XPManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.PartyFactory
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit


class homepage : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var db: CourseDatabase
    private val sortedList = mutableListOf<UpcomingItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_homepage, container, false)
        recycler = view.findViewById(R.id.recyclerUpcoming)
        recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        db = CourseDatabase.getDatabase(requireContext())
        loadUpcoming()
        val streakTextView = view.findViewById<TextView>(R.id.tvStreakStatus)
        val progressBar = view.findViewById<ProgressBar>(R.id.streakProgress)

        val streakManager = StreakManager(requireContext())
        val result = streakManager.updateStreak()

        streakTextView.text = "🔥 You’re on a ${result.count}-day streak!"
        progressBar.progress = result.count

        result.milestoneMessage?.let {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            // Optionally: animate flame or show XP
        }
        if (result.brokeStreak && result.count == 1) {
            Toast.makeText(requireContext(), "You got this! Let’s start a new streak 🔄", Toast.LENGTH_SHORT).show()
        }

        // Goals
        val checkStudyToday = view.findViewById<CheckBox>(R.id.checkStudyToday)
        val dueTaskText = view.findViewById<TextView>(R.id.tvDueTask)
        val goalManager = DailyGoalManager(requireContext())
        val xpManager = XPManager(requireContext())
        goalManager.resetIfNeeded()
        // Today sorting


        val today = LocalDate.now()
        val dueTodayItems = sortedList.filter {
            parseDateTime(it.dateTime)?.toLocalDate() == today
        }

        if (dueTodayItems.isNotEmpty()) {
            val firstItem = dueTodayItems[0]
            dueTaskText.text = "Due today: ${firstItem.courseName} - ${firstItem.title}"
        } else {
            dueTaskText.text = "No deadlines today 🎉"
        }


        checkStudyToday.isChecked = goalManager.isGoalDone("goal_study_done")

        checkStudyToday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                goalManager.setGoalDone("goal_study_done")
                goalManager.addXp(10)
                xpManager.addXP(10)
                val xp = DailyGoalManager(requireContext()).getXp()
                Log.d("XP_DEBUG", "XP added: 10, Total XP: ${xp}")

                refreshXPBar()

                Toast.makeText(requireContext(), "+10 XP for studying today!", Toast.LENGTH_SHORT).show()
                val konfettiView = view?.findViewById<nl.dionsegijn.konfetti.xml.KonfettiView>(R.id.konfettiView)
                konfettiView?.visibility = View.VISIBLE

                val party = Party(
                    emitter = Emitter(duration = 1500, TimeUnit.MILLISECONDS).max(300),
                    position = Position.Relative(0.5, 0.3),
                    speed = 10f,
                    maxSpeed = 30f,
                    damping = 0.9f,
                    spread = 360,
                    colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def)
                )


                konfettiView?.start(party)


            }
        }
        // Track our xps


        val xpText = view.findViewById<TextView>(R.id.xpLevelText)
        val xpBar = view.findViewById<ProgressBar>(R.id.xpProgressBar)
        val xpHint = view.findViewById<TextView>(R.id.xpHintText)

        val level = xpManager.getLevel()
        val progress = xpManager.getXPProgress()
        val toNext = xpManager.getXPToNextLevel()

        xpText.text = "Level $level"
        xpBar.progress = progress
        xpHint.text = "$toNext XP to next level"

        //open study
        view.findViewById<Button>(R.id.btnStudyTechniques).setOnClickListener {
            val intent = Intent(requireContext(), StudyTechniquesActivity::class.java)
            startActivity(intent)
        }




        return view
    }
    private fun parseDateTime(dateTime: String): LocalDateTime? {
        val patterns = listOf(
            "d/M/yyyy - H:mm",
            "dd/MM/yyyy - HH:mm",
            "d/M/yyyy - HH:mm",
            "dd/M/yyyy - H:mm"
        )

        for (pattern in patterns) {
            try {
                val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
                return LocalDateTime.parse(dateTime, formatter)
            } catch (_: Exception) {}
        }

        return null
    }


    private fun loadUpcoming() {
        lifecycleScope.launch {
            val courseList = db.courseDao().getAllCourses()
            val upcomingList = mutableListOf<UpcomingItem>()

            for (course in courseList) {
                // Extract classes
                val classList: List<Pair<String, String>> =
                    if (!course.classTimes.isNullOrEmpty() && course.classTimes != "null") {
                        Gson().fromJson(course.classTimes, object : TypeToken<List<Pair<String, String>>>() {}.type)
                    } else emptyList()
                classList.forEach {
                    upcomingList.add(
                        UpcomingItem("class", "${it.first} class", it.second, course.courseName)
                    )
                }

                // Extract CATs
                val catList: List<String> =
                    if (!course.catDates.isNullOrEmpty() && course.catDates != "null") {
                        Gson().fromJson(course.catDates, object : TypeToken<List<String>>() {}.type)
                    } else emptyList()
                catList.forEach {
                    upcomingList.add(
                        UpcomingItem("cat", "CAT", it, course.courseName)
                    )
                }

                // Extract Exam
                course.examDateTime?.let {
                    if (it.isNotBlank()) {
                        upcomingList.add(
                            UpcomingItem("exam", "Exam", it, course.courseName)
                        )
                    }
                }
            }

            // 🔍 Log all parsed dates
            for (item in upcomingList) {
                val parsed = parseDateTime(item.dateTime)
                android.util.Log.d("DATE_PARSE", "Raw: ${item.dateTime} → Parsed: $parsed")
            }

            val now = LocalDateTime.now()

            sortedList.clear()
            sortedList.addAll(
                upcomingList
                    .filter { item ->
                        val parsed = parseDateTime(item.dateTime)
                        parsed != null && parsed.isAfter(now)
                    }
                    .sortedBy {
                        parseDateTime(it.dateTime) ?: LocalDateTime.MIN
                    }
            )


            recycler.adapter = UpcomingAdapter(sortedList)
        }
    }

    private fun refreshXPBar() {
        val xpManager = XPManager(requireContext())
        val xpText = view?.findViewById<TextView>(R.id.xpLevelText)
        val xpBar = view?.findViewById<ProgressBar>(R.id.xpProgressBar)
        val xpHint = view?.findViewById<TextView>(R.id.xpHintText)

        val level = xpManager.getLevel()
        val progress = xpManager.getXPProgress()
        val toNext = xpManager.getXPToNextLevel()

        xpText?.text = "Level $level"
        xpBar?.progress = progress
        xpHint?.text = "$toNext XP to next level"
    }


}
