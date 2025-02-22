package com.example.studypool.appctrl
import android.content.Context
import android.content.SharedPreferences


class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)

    companion object {
        private const val ADMIN_TOKEN_KEY = "admin_token"
        private const val USER_TOKEN_KEY = "user_token"
        private const val REAL_NAME_KEY = "real_name"
        private const val USER_ID = "user_id"
    }

    // Save admin token to SharedPreferences
    fun saveAdminToken(token: String) {
        val editor = prefs.edit()
        editor.putString(ADMIN_TOKEN_KEY, token)
        editor.apply()
    }

    // Save user token to SharedPreferences
    fun saveUserToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN_KEY, token)
        editor.apply()
    }

    // Retrieve admin token from SharedPreferences
    fun fetchAdminToken(): String? {
        return prefs.getString(ADMIN_TOKEN_KEY, null)
    }

    // Retrieve user token from SharedPreferences
    fun fetchUserToken(): String? {
        return prefs.getString(USER_TOKEN_KEY, null)
    }

    // Save real name to SharedPreferences
    fun saveRealName(realName: String) {
        val editor = prefs.edit()
        editor.putString(REAL_NAME_KEY, realName)
        editor.apply()
    }
    fun fetchRealName():String?{
        return prefs.getString(REAL_NAME_KEY,null)
    }
    fun fetchUserid():String?{
        return prefs.getString(USER_ID,null)
    }


    // Clear tokens (called during logout)
    fun clearSession() {
        val editor = prefs.edit()
        editor.remove(ADMIN_TOKEN_KEY)  // Remove the admin token
        editor.remove(USER_TOKEN_KEY)   // Remove the user token
        editor.apply()
    }
}
