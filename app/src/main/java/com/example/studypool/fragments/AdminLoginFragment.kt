package com.example.studypool.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.studypool.R
import com.example.studypool.admin.AdminDashboardActivity
import com.example.studypool.appctrl.AdminLoginRequest
import com.example.studypool.appctrl.AdminLoginResponse
import com.example.studypool.appctrl.RetrofitClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminLoginFragment : Fragment() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var loadingBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_admin_login, container, false)

        emailInput = view.findViewById(R.id.adminEmail)
        passwordInput = view.findViewById(R.id.adminPassword)
        loginButton = view.findViewById(R.id.adminLoginBtn)
        loadingBar = ProgressBar(requireContext()).apply { visibility = View.GONE }

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                loginAdmin(email, password)
            }
        }

        return view
    }

    private fun loginAdmin(email: String, password: String) {
        val request = AdminLoginRequest(email, password)
        loginButton.isEnabled = false
        loadingBar.visibility = View.VISIBLE

        RetrofitClient.apiService.loginStudyAdmin(request).enqueue(object : Callback<AdminLoginResponse> {
            override fun onResponse(call: Call<AdminLoginResponse>, response: Response<AdminLoginResponse>) {
                loginButton.isEnabled = true
                loadingBar.visibility = View.GONE

                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.success) {
                        Log.d("ADMIN_LOGIN", "✅ Success: ${body.message}")
                        Toast.makeText(requireContext(), body.message, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(requireContext(), AdminDashboardActivity::class.java))
                    } else {
                        Log.e("ADMIN_LOGIN", "❌ Login failed: ${body.message}")
                        Toast.makeText(requireContext(), body.message ?: "Login failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMsg = response.errorBody()?.string()
                    Log.e("ADMIN_LOGIN", "❌ Server error: $errorMsg")
                    Toast.makeText(requireContext(), "Server error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AdminLoginResponse>, t: Throwable) {
                loginButton.isEnabled = true
                loadingBar.visibility = View.GONE

                Log.e("ADMIN_LOGIN", "❌ Network error: ${t.message}", t)
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
