package com.example.studypool.admin

import AdminUserAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studypool.R
import com.example.studypool.database.CourseDatabase
import com.example.studypool.database.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminUsersActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminUserAdapter
    private lateinit var db: CourseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_users)

        recyclerView = findViewById(R.id.recyclerUsers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        db = CourseDatabase.getDatabase(this)
        loadUsers()
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            val users = withContext(Dispatchers.IO) {
                db.userDao().getAllUsers()
            }



            adapter = AdminUserAdapter(users) { user ->
                confirmDelete(user)
            }
            recyclerView.adapter = adapter
        }
    }

    private fun confirmDelete(user: UserEntity) {
        AlertDialog.Builder(this)
            .setTitle("Delete User")
            .setMessage("Are you sure you want to remove ${user.email}?")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        db.userDao().deleteUser(user)
                    }
                    Toast.makeText(this@AdminUsersActivity, "User deleted", Toast.LENGTH_SHORT).show()
                    loadUsers()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
