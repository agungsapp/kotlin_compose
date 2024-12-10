package com.example.tbptb.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.tbptb.data.AuthResponse
import com.example.tbptb.data.RegisterResponse
import com.example.tbptb.data.RegisterRequest
import com.example.tbptb.network.ApiClient
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel : ViewModel() {

    // Fungsi untuk melakukan registrasi
    fun register(registerRequest: RegisterRequest, navController: NavController, context: Context) {
        val authService = ApiClient.apiService
        Log.d("REQ:", registerRequest.email+"@@"+registerRequest.name+"@@"+registerRequest.password);
        val call = authService.register(registerRequest)

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                Log.d("RESP:", response.toString());

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.success == true) {
                        Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT)
                            .show()
                        navController.navigate("Login")
                    } else {
                        Toast.makeText(
                            context,
                            "Registration Failed: ${body?.message ?: "Unknown error"}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Registration Failed: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(context, "Registration Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }}


