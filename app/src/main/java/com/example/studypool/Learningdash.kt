package com.example.studypool

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class Learningdash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_learningdash)
        val homebtn=findViewById<Button>(R.id.Home)
        val fileuploadbtn=findViewById<Button>(R.id.File)
        val assistantbtn=findViewById<Button>(R.id.Assistant)
        val profilebtn=findViewById<Button>(R.id.Profile)
        val dashboardframe=findViewById<FrameLayout>(R.id.framedash)

        //setting up fragments into the frame through the buttons
        loadFragment(homepage())
        homebtn.setOnClickListener {
            loadFragment(homepage())
        }
        fileuploadbtn.setOnClickListener {
            loadFragment(fileupload())
        }
        assistantbtn.setOnClickListener {
            loadFragment(assistant())
        }
        profilebtn.setOnClickListener {
            loadFragment(profile())
        }

    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framedash, fragment)
        transaction.addToBackStack(null) // Enables back navigation
        transaction.commit()
    }

}