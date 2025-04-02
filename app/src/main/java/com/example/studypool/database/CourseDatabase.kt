package com.example.studypool.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CourseEntity::class, UserEntity::class], // 👈 Add UserEntity here
    version = 5,
    exportSchema = false
)
abstract class CourseDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
    abstract fun userDao(): UserDao  // 👈 Add this

    companion object {
        @Volatile private var INSTANCE: CourseDatabase? = null

        fun getDatabase(context: Context): CourseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CourseDatabase::class.java,
                    "study_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
