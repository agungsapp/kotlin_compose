package com.example.tbptb.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.tbptb.data.AuthRequest
import com.example.tbptb.data.AuthResponse
import com.example.tbptb.network.ApiClient // Pastikan ini sesuai dengan nama file
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    // Fungsi untuk melakukan login menggunakan Retrofit
    fun login(authRequest: AuthRequest, navController: NavController, context: Context) {
        val authService = ApiClient.apiService // Sesuaikan dengan instance Retrofit Anda
        val call = authService.login(authRequest)

        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    // Pastikan body.data tidak null dan token ada
                    if (body?.data?.token != null) {
                        val token = body.data.token
                        // Menyimpan token ke SharedPreferences atau tempat lain jika diperlukan
                        saveToken(context, token)
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                        navController.navigate("Dashboard") // Navigasi ke Dashboard
                    } else {
                        Toast.makeText(context, "Login Failed: ${body?.message ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Login Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(context, "Login Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Fungsi untuk menyimpan token (gunakan SharedPreferences atau metode lainnya)
    private fun saveToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)
        editor.apply()
    }
}
