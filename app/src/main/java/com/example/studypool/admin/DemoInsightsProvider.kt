package com.example.studypool.admin

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class DemoUserSession(
    val email: String,
    val course: String,
    val sessionDurationMin: Int,
    val date: String,
    val xpGained: Int
)

object DemoInsightsProvider {

    fun getDemoSessions(): List<DemoUserSession> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now()

        return listOf(
            DemoUserSession("alice@campus.edu", "Intro to Calculus", 25, today.toString(), 20),
            DemoUserSession("bob@campus.edu", "Intro to Calculus", 22, today.toString(), 18),
            DemoUserSession("carol@campus.edu", "Physics I", 15, today.minusDays(1).toString(), 10),
            DemoUserSession("dave@campus.edu", "Data Structures", 30, today.minusDays(1).toString(), 25),
            DemoUserSession("eve@campus.edu", "Linear Algebra", 10, today.minusDays(2).toString(), 8),
            DemoUserSession("frank@campus.edu", "Linear Algebra", 15, today.minusDays(2).toString(), 12),
            DemoUserSession("grace@campus.edu", "Microeconomics", 20, today.minusDays(2).toString(), 15),
            DemoUserSession("heidi@campus.edu", "Intro to Calculus", 40, today.minusDays(2).toString(), 30),
            DemoUserSession("ivan@campus.edu", "Microeconomics", 5, today.minusDays(3).toString(), 3),
            DemoUserSession("judy@campus.edu", "Physics I", 25, today.minusDays(3).toString(), 20),
            DemoUserSession("kate@campus.edu", "Data Structures", 35, today.minusDays(3).toString(), 28),
            DemoUserSession("leo@campus.edu", "Intro to Calculus", 25, today.minusDays(3).toString(), 22),
            DemoUserSession("mallory@campus.edu", "Intro to Calculus", 15, today.minusDays(4).toString(), 10),
            DemoUserSession("nick@campus.edu", "Microeconomics", 20, today.minusDays(4).toString(), 16),
            DemoUserSession("oscar@campus.edu", "Physics I", 10, today.minusDays(4).toString(), 7),
            DemoUserSession("peggy@campus.edu", "Data Structures", 12, today.minusDays(4).toString(), 10),
            DemoUserSession("quinn@campus.edu", "Linear Algebra", 22, today.minusDays(5).toString(), 19),
            DemoUserSession("rachel@campus.edu", "Intro to Calculus", 18, today.minusDays(5).toString(), 15),
            DemoUserSession("sybil@campus.edu", "Physics I", 5, today.minusDays(5).toString(), 3),
            DemoUserSession("trent@campus.edu", "Microeconomics", 8, today.minusDays(5).toString(), 4)
        )
    }
}
