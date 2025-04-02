package com.example.studypool.admin

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.studypool.R
import com.example.studypool.appctrl.GenericResponse
import com.example.studypool.appctrl.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminSignupFragment : Fragment() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmInput: EditText
    private lateinit var signupButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_admin_signup, container, false)

        emailInput = view.findViewById(R.id.signupEmail)
        passwordInput = view.findViewById(R.id.signupPassword)
        confirmInput = view.findViewById(R.id.signupConfirmPassword)
        signupButton = view.findViewById(R.id.signupButton)

        signupButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val pass = passwordInput.text.toString().trim()
            val confirm = confirmInput.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
            } else if (pass != confirm) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                registerAdmin(email, pass)
            }
        }

        return view
    }

    private fun registerAdmin(email: String, password: String) {
        val payload = mapOf("email" to email, "password" to password)

        RetrofitClient.apiService.registerStudyAdmin(payload).enqueue(object : Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Toast.makeText(context, body.message, Toast.LENGTH_SHORT).show()
                    Log.d("ADMIN_SIGNUP", "✅ ${body.message}")
                } else {
                    Log.e("ADMIN_SIGNUP", "❌ ${response.errorBody()?.string()}")
                    Toast.makeText(context, "Signup failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                Log.e("ADMIN_SIGNUP", "❌ ${t.message}", t)
                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
