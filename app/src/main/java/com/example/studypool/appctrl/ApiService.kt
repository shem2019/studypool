package com.example.studypool.appctrl


import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

// Data model for sending signup request
data class SignupRequest(
    val username: String,
    val password: String
)

// Data model for handling signup response
data class SignupResponse(
    val status: String,
    val message: String,
    val token: String? // Token is returned only on success
)

data class LoginRequest(
    val username: String,
    val password: String
)

// Data model for login response
data class LoginResponse(
    val status: String,
    val token: String?,  // Token will be returned on success
    val message: String? // Error message in case of failure
)



data class RegisterUserRequest(val real_name: String, val username: String, val password: String)
data class RegisterUserResponse(val status: String, val message: String, val user_id: Int, val token: String)
data class UserResponse(val user_id: Int, val real_name: String, val username: String)

data class UserLoginRequest(val username: String, val password: String)
data class UserLoginResponse(val status: String, val token: String?, val message: String?)

data class UserDetailsResponse(
    val status: String,
    val data: UserDetailsData?
)

data class UserDetailsData(
    val real_name: String,
    val username: String
)
data class SaveEventRequest(
    val event_type: String,
    val site_name: String,
    val real_name: String,
    val timestamp: String
)
data class SaveLogRequest(
    val event_type: String
)


data class Course(
    var courseName: String,
    var courseMaterialUri: String?, // File URI
    var classTimes: MutableList<Pair<String, String>>, // Day + Time
    var examDateTime: String?, // Single exam date & time
    var catDateTimes: MutableList<String> // List for multiple CATs
)

data class UpcomingItem(
    val type: String, // "class", "cat", "exam"
    val title: String,
    val dateTime: String,
    val courseName: String
)

data class ApiResponse(
    val success: Boolean,
    val message: String
)
data class AdminLoginRequest(val email: String, val password: String)
data class AdminLoginResponse(val success: Boolean, val message: String)


data class GenericResponse(
    val success: Boolean,
    val message: String
)
data class LeaderboardItem(
    val name: String,
    val xp: Int
)

interface ApiService {
    //logs
    @POST("savelog.php")
    fun saveLog(
        @Header("Authorization") token: String,
        @Body logRequest: SaveLogRequest
    ): Call<Void>
    //events
    @GET("userdatafetch.php")
    fun getUserDetails(@Header("Authorization") token: String): Call<UserDetailsResponse>
    @Headers("Content-Type: application/json")
    @POST("admin_signup.php") // This is the API endpoint on your server
    fun signupAdmin(@Body signupRequest: SignupRequest): Call<SignupResponse>
    //admin login
    @Headers("Content-Type: application/json")
    @POST("admin_login.php")  // This is the API endpoint on your server
    fun loginAdmin(@Body loginRequest: LoginRequest): Call<LoginResponse>



    @POST("register_user.php")
    fun registerUser(@Body request: RegisterUserRequest): Call<RegisterUserResponse>


    @POST("user_login.php")
    fun loginUser(@Body request: UserLoginRequest): Call<UserLoginResponse>

    @FormUrlEncoded
    @POST("studyadminlogin.php")
    fun loginAdmin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ApiResponse>

    @POST("studyadminlogin.php")
    fun loginAdminSecure(
        @Body body: Map<String, String>
    ): Call<AdminLoginResponse>

    @POST("studyadminlogin.php")
    fun loginStudyAdmin(@Body request: AdminLoginRequest): Call<AdminLoginResponse>

    @POST("studyadmin_register.php")
    fun registerStudyAdmin(@Body body: Map<String, String>): Call<GenericResponse>




}


