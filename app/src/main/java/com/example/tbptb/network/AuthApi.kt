package com.example.tbptb.network
import com.example.tbptb.data.AuthRequest
import com.example.tbptb.data.AuthResponse
import com.example.tbptb.data.RegisterRequest
import com.example.tbptb.data.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/login")
    fun login(@Body request: AuthRequest): Call<AuthResponse>
    @POST("api/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>
}
