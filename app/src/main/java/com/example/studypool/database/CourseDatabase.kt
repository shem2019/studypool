package com.example.studypool.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [CourseEntity::class], version = 3, exportSchema = false)  // ✅ Increase version number
abstract class CourseDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao

    companion object {
        @Volatile private var instance: CourseDatabase? = null

        fun getDatabase(context: Context): CourseDatabase {
            return instance ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    CourseDatabase::class.java,
                    "study_database"
                )
                    .fallbackToDestructiveMigration() // ✅ Auto-reset database on schema changes
                    .build()
                instance = db
                db
            }
        }
    }
}
