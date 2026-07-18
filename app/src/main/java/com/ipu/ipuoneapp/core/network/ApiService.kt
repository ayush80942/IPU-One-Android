package com.ipu.ipuoneapp.core.network

import com.ipu.ipuoneapp.data.model.auth.AuthResponse
import com.ipu.ipuoneapp.data.model.auth.AuthStatusResponse
import com.ipu.ipuoneapp.data.model.auth.CaptchaResponse
import com.ipu.ipuoneapp.data.model.auth.GoogleLoginRequest
import com.ipu.ipuoneapp.data.model.auth.ImportRequest
import com.ipu.ipuoneapp.data.model.auth.ImportResponse
import com.ipu.ipuoneapp.data.model.auth.MessageResponse
import com.ipu.ipuoneapp.data.model.result.ResultDashboardDTO
import com.ipu.ipuoneapp.data.model.student.StudentProfileDTO
import com.ipu.ipuoneapp.data.model.notice.NoticeResponseDto
import com.ipu.ipuoneapp.data.model.notice.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("api/auth/google")
    suspend fun loginWithGoogle(
        @Body request: GoogleLoginRequest
    ): AuthResponse

    @POST("api/auth/send-otp")
    suspend fun sendOtp(
        @Query("email") email: String
    ): MessageResponse

    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(
        @Query("email") email: String,
        @Query("otp") otp: String
    ): AuthResponse

    @GET("/api/ipu/captcha")
    suspend fun getCaptcha(): CaptchaResponse

    @POST("/api/ipu/import")
    suspend fun importResult(
        @Body request: ImportRequest
    ): ImportResponse
    @GET("api/auth/status")
    suspend fun getAuthStatus(): AuthStatusResponse

    @GET("api/result")
    suspend fun getResultDashboard(): ResultDashboardDTO

    @GET("api/student/profile")
    suspend fun getStudentProfile(): StudentProfileDTO

    @GET("api/notices")
    suspend fun getNotices(
        @Query("category") category: String? = null,
        @Query("search") search: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 50
    ): PageResponse<NoticeResponseDto>

    @POST("api/documents/collect")
    suspend fun submitDocument(
        @Body request: com.ipu.ipuoneapp.data.model.document.DocumentRequestDto
    ): com.ipu.ipuoneapp.data.model.document.DocumentResponseDto

    @GET("api/documents")
    suspend fun getMyDocuments(): List<com.ipu.ipuoneapp.data.model.document.DocumentResponseDto>
}