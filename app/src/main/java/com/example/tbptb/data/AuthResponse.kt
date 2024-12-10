package com.example.tbptb.data

// Respons untuk Login
data class AuthResponse(
    val success: Boolean,
    val message: String,
    val data: AuthData?
)

// Data yang dikembalikan saat login sukses
data class AuthData(
    val token: String?,
    val user: User?
)

// Data pengguna
data class User(
    val id: String,
    val email: String,
    val nama: String
)