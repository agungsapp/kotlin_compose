package com.example.tbptb.data
// Respons untuk Register
data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val data: RegisterData?
)

// Data yang dikembalikan saat registrasi sukses
data class RegisterData(
    val user: User // Mengembalikan data pengguna setelah registrasi
)

