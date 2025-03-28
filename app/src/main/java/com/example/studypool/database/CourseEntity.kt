package com.example.studypool.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val courseName: String,
    val examDateTime: String? = null,
    val catDates: String = "[]",
    val classTimes: String = "[]",
    val fileUris: String = "[]"
)
