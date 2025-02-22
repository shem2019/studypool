package com.example.studypool.appctrl


import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
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
data class StoreResponse(
    val status: String,
    val message: String?
)


data class NextUserIdResponse(val next_user_id: Int)
data class RegisterUserRequest(val real_name: String, val username: String, val password: String)
data class RegisterUserResponse(val status: String, val message: String, val user_id: Int, val token: String)
data class UserResponse(val user_id: Int, val real_name: String, val username: String)
data class UpdatePasswordRequest(val user_id: Int, val new_password: String)
data class UpdatePasswordResponse(val status: String, val message: String)
data class UserLoginRequest(val username: String, val password: String)
data class UserLoginResponse(val status: String, val token: String?, val message: String?)
data class FacialDataRequest(
    val token: String,
    val embedding: List<Float>
)
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
data class SaveHourlyCheckRequest(
    val token: String, // References `token` column in `users`
    val site_id: Int, // References `site_id` column in `sites`
    val personal_safety: Boolean,
    val site_secure: Boolean,
    val equipment_functional: Boolean,
    val comments: String? // Optional comments field
)


data class SitesResponse(
    val success: Boolean,
    val sites: List<Site>
)
data class Site(
    val id: Int,
    val name: String,
    val location: String?,
    val latitude: Double,
    val longitude: Double
)
data class RegisterOrUpdateHouseRequest(
    val house_number: String,
    val alert_numbers: String
)

// Data model for the response from register or update house
data class RegisterOrUpdateHouseResponse(
    val status: String,
    val message: String
)

// Data model for a single house (used in fetching houses)
data class House(
    val id: Int,
    val house_number: String,
    val alert_numbers: String
)

// Data model for the response when fetching houses
data class FetchHousesResponse(
    val status: String,
    val houses: List<House>
)

// Data model for fetching visitor records
data class VisitorRecord(
    val id: Int,
    val house_id: Int,
    val house_number: String, // Include house number for reference
    val description: String,
    val name: String,
    val phone_number: String,
    val admitted_by: String,
    val created_at: String
)

// Data model for the response when fetching visitor records
//data class FetchVisitorRecordsResponse(
    //val status: String,
    //val visitor_records: List<VisitorRecord>
//)
//
data class UploadImageResponse(
    val status: String,
    val imageUrl: String,
    val message: String?
)

//

data class VisitorRecordRequest(
    val house_id: Int,
    val description: String,
    val name: String,
    val phone_number: String,
    val admitted_by: String,
    val image_metadata: String?, // Optional image metadata
    val id_plates: String?, // Optional ID/plates
    val otp_code: String
)

data class VisitorRecordResponse(
    val status: String,
    val message: String,
    val image_metadata: String? // URL or metadata of the image
)
data class ValidateOTPRequest(val otp_code: String)
data class ValidateOTPResponse(
    val isValid: Boolean,
    val message: String
)

data class MarkVisitorExitedRequest(val otp_code: String)
data class Visitor(
    val id: Int,
    val house_id: Int,
    val house_number: String, // Added this field
    val description: String,
    val name: String,
    val phone_number: String,
    val image_metadata: String,
    val id_plates: String?,
    val admitted_by: String,
    val created_at: String,
    val otp_code: String,
    val is_exited: Int
)
data class FetchVisitorRecordsResponse(
    val status: String,
    val visitor_records: List<Visitor>
)

data class Course(
    var courseName: String,
    var courseMaterialUri: String?, // File URI
    var classTimes: MutableList<Pair<String, String>>, // Day + Time
    var examDateTime: String?, // Single exam date & time
    var catDateTimes: MutableList<String> // List for multiple CATs
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

    @GET("get_next_user_id.php")
    fun getNextUserId(): Call<NextUserIdResponse>

    @POST("register_user.php")
    fun registerUser(@Body request: RegisterUserRequest): Call<RegisterUserResponse>

    @GET("fetch_users.php")
    fun fetchUsers(@Query("search") searchTerm: String): Call<List<UserResponse>>

    @POST("update_password.php")
    fun updatePassword(@Body request: UpdatePasswordRequest): Call<UpdatePasswordResponse>
    @POST("user_login.php")
    fun loginUser(@Body request: UserLoginRequest): Call<UserLoginResponse>

    //




    //Houses
    @POST("house_post.php")
    fun registerOrUpdateHouse(
        @Body request: RegisterOrUpdateHouseRequest
    ): Call<RegisterOrUpdateHouseResponse>

        // GET: Fetch all houses
    @GET("house_get.php")
    fun fetchHouses(): Call<FetchHousesResponse>

    // GET: Fetch all visitor records
    @GET("visitor_get.php")
    fun fetchVisitorRecords(): Call<FetchVisitorRecordsResponse>

    //Image
    @Multipart
    @POST("visitor/submit")
    fun submitVisitorRecord(
        @Part("house_id") houseId: RequestBody,
        @Part("description") description: RequestBody,
        @Part("name") name: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
        @Part("admitted_by") admittedBy: RequestBody,
        @Part("image_metadata") imageMetadata: RequestBody,
        @Part("id_plates") idPlates: RequestBody?
    ): Call<Void>

    @Multipart
    @POST("visitorphoto.php")
    fun uploadVisitorImage(
        @Part image: MultipartBody.Part,
        @Part("user_id") userId: RequestBody,
        @Part("house_id") houseId: RequestBody,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<UploadImageResponse>


    //
    @POST("visitor_post.php")
    fun sendVisitorRecord(
        @Body request: VisitorRecordRequest
    ): Call<VisitorRecordResponse>

    @POST("validate_otp.php")
    fun validateOTP(@Body request: ValidateOTPRequest): Call<ValidateOTPResponse>


    @POST("mark_visitor_exited.php")
    fun markVisitorExited(@Body request: ValidateOTPRequest): Call<Void>

    @GET("visitor_get.php")
    fun getVisitors(): Call<FetchVisitorRecordsResponse>

}


