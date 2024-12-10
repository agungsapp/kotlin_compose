package com.example.tbptb.network

import com.example.tbptb.data.AuthRequest
import com.example.tbptb.data.AuthResponse
import com.example.tbptb.data.RegisterRequest
import com.example.tbptb.data.RegisterResponse
import com.example.tbptb.data.UpdatePasswordRequest
import com.example.tbptb.data.UpdateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ApiService {
    @POST("api/login") // Endpoint untuk login
    fun login(@Body request: AuthRequest): Call<AuthResponse>

    @POST("api/register") // Endpoint untuk registrasi
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @PATCH("/api/me")
    fun updatePassword(
        @Header("Authorization") token: String,
        @Body updateRequest: UpdatePasswordRequest
    ): Call<UpdateResponse>
}
