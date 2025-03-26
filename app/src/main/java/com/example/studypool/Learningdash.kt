package com.example.studypool

import android.content.pm.PackageManager
import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.studypool.fragments.FileUploadFragment
import com.example.studypool.fragments.StudyFragment
import com.example.studypool.fragments.UpcomingFragment

class Learningdash : AppCompatActivity() {
    companion object {
        private const val REQUEST_STORAGE_PERMISSION = 100
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_learningdash)
        val homebtn=findViewById<Button>(R.id.Home)
        val fileuploadbtn=findViewById<Button>(R.id.File)
        val assistantbtn=findViewById<Button>(R.id.Assistant)
        val profilebtn=findViewById<Button>(R.id.Profile)
        val studybtn=findViewById<Button>(R.id.study)
        val dashboardframe=findViewById<FrameLayout>(R.id.framedash)

        //setting up fragments into the frame through the buttons
        loadFragment(homepage())
        homebtn.setOnClickListener {
            loadFragment(homepage())
        }
        fileuploadbtn.setOnClickListener {
            loadFragment(FileUploadFragment())
        }
        studybtn.setOnClickListener {
            loadFragment(StudyFragment())
        }

        assistantbtn.setOnClickListener {
            loadFragment(assistant())
        }
        profilebtn.setOnClickListener {
            loadFragment(profile())
        }
        requestPermissions()
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framedash, fragment)
        transaction.addToBackStack(null) // Enables back navigation
        transaction.commit()
    }

    private fun requestPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            permissions.add(Manifest.permission.READ_MEDIA_VIDEO)
            permissions.add(Manifest.permission.READ_MEDIA_AUDIO)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissions.isNotEmpty()) {
            requestPermissionLauncher.launch(permissions.toTypedArray())
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.values.all { it }) {
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show()
            }
        }
}