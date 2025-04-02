package com.example.studypool

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.studypool.admin.AdminSignupFragment
import com.example.studypool.fragments.AdminLoginFragment

class MainActivity : AppCompatActivity() {

    private lateinit var signuptv: TextView
    private lateinit var logintv: TextView
    private lateinit var adminloginbtn: Button
    private lateinit var adminsignbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signuptv = findViewById(R.id.TVreg)
        logintv = findViewById(R.id.TVlogin)
        adminloginbtn = findViewById(R.id.adminloginbtn)
        adminsignbtn = findViewById(R.id.adminsignbtn)

        // Default: show user login
        loadFragment(UserLoginFragment())
        updateToggleUI(forUser = true, isSignup = false)

        // User signup click
        signuptv.setOnClickListener {
            loadFragment(usersignup())
            updateToggleUI(forUser = true, isSignup = true)
        }

        // User login click
        logintv.setOnClickListener {
            loadFragment(UserLoginFragment())
            updateToggleUI(forUser = true, isSignup = false)
        }

        // Admin login
        adminloginbtn.setOnClickListener {
            loadFragment(AdminLoginFragment())
            updateToggleUI(forUser = false, isSignup = false)
        }

        // Admin signup
        adminsignbtn.setOnClickListener {
            loadFragment(AdminSignupFragment())
            updateToggleUI(forUser = false, isSignup = true)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.framelayout, fragment)
            .addToBackStack(null)
            .commit()
    }

    // Toggle text visibility based on current view
    private fun updateToggleUI(forUser: Boolean, isSignup: Boolean) {
        if (forUser) {
            signuptv.visibility = if (isSignup) View.GONE else View.VISIBLE
            logintv.visibility = if (isSignup) View.VISIBLE else View.GONE
        } else {
            // Hide both for admin (optional - or keep to prevent confusion)
            signuptv.visibility = View.GONE
            logintv.visibility = View.GONE
        }
    }
}
