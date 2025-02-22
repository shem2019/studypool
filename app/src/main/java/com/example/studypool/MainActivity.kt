package com.example.studypool

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val signuptv=findViewById<TextView>(R.id.TVreg)
        val logintv=findViewById<TextView>(R.id.TVlogin)

        // Load user login fragment when the activity starts
        loadFragment(UserLoginFragment())

        signuptv.setOnClickListener {
            loadFragment(usersignup())
            signuptv.visibility= View.GONE
            logintv.visibility=View.VISIBLE

        }
        logintv.setOnClickListener {
            loadFragment(UserLoginFragment())
            signuptv.visibility= View.VISIBLE
            logintv.visibility=View.GONE
        }


    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framelayout, fragment)
        transaction.addToBackStack(null) // Enables back navigation
        transaction.commit()
    }
}
