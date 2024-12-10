package com.example.tbptb.data

data class RegisterRequest(
    val name: String,      // Nama pengguna
    val email: String,     // Email pengguna
    val password: String   // Password pengguna
)
