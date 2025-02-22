package com.example.studypool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.studypool.appctrl.RegisterUserRequest
import com.example.studypool.appctrl.RegisterUserResponse
import com.example.studypool.appctrl.RetrofitClient
import com.example.studypool.databinding.FragmentUsersignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class usersignup : Fragment() {
    private var _binding: FragmentUsersignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize View Binding
        _binding = FragmentUsersignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Register User button click listener
        binding.btnRegisterUser.setOnClickListener {
            registerUser()
        }
    }



    // Register the user by sending their details to the server
    private fun registerUser() {
        val realName = binding.etRealName.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (realName.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
            val userRequest = RegisterUserRequest(realName, username, password)

            RetrofitClient.apiService.registerUser(userRequest).enqueue(object : Callback<RegisterUserResponse> {
                override fun onResponse(call: Call<RegisterUserResponse>, response: Response<RegisterUserResponse>) {
                    if (response.isSuccessful && response.body()?.status == "success") {
                        Toast.makeText(requireContext(), "User registered successfully", Toast.LENGTH_SHORT).show()
                        clearInputFields()
                    } else {
                        Toast.makeText(requireContext(), "Failed to register user", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    // Clear input fields after successful registration
    private fun clearInputFields() {
        binding.etRealName.text?.clear()
        binding.etUsername.text?.clear()
        binding.etPassword.text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear binding reference
    }
}