package com.example.tbptb.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // URL dasar API
     const val BASE_URL = "https://api-unand-research-875600580548.asia-southeast2.run.app" // Ganti dengan URL server API-mu

    // Membuat instance Retrofit
    // Inisialisasi Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Buat instance API service
    val apiService: AuthApi = retrofit.create(AuthApi::class.java)
}
