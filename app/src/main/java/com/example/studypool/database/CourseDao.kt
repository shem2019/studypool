package com.example.studypool.database

import androidx.room.*

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseEntity)

    @Query("SELECT * FROM courses")
    suspend fun getAllCourses(): List<CourseEntity>
    @Delete
    suspend fun deleteCourse(course: CourseEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCourses(courses: List<CourseEntity>)

}
