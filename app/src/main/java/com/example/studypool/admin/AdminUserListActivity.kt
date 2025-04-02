package com.example.studypool.admin

import AdminUserAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studypool.R
import com.example.studypool.database.CourseDatabase
import com.example.studypool.database.UserEntity
import kotlinx.coroutines.launch

class AdminUserListActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var db: CourseDatabase  // ✅ Move db here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_list)

        recycler = findViewById(R.id.recyclerAdminUsers)
        recycler.layoutManager = LinearLayoutManager(this)

        db = CourseDatabase.getDatabase(this)

        // ✅ Load users initially
        loadUsers()

        // Optionally load demo data
        findViewById<Button>(R.id.btnLoadDemoUsers).setOnClickListener {
            insertDemoUsers()
        }

    }

    private fun loadUsers() {
        lifecycleScope.launch {
            val users = db.userDao().getAllUsers()
            recycler.adapter = AdminUserAdapter(users)
        }
    }

    private fun insertDemoUsers() {
        val users = listOf(
            UserEntity(id = 1, email = "alice@student.com", password = "123456"),
            UserEntity(id = 2, email = "bob@student.com", password = "abcdef"),
            UserEntity(id = 3, email = "carol@student.com", password = "pass123")
        )

        lifecycleScope.launch {
            db.userDao().insertAll(users)
            Toast.makeText(this@AdminUserListActivity, "Demo users added", Toast.LENGTH_SHORT).show()
            loadUsers()
        }
    }
}
