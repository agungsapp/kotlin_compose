package com.example.tbptb

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import android.widget.Toast
import com.example.tbptb.data.UpdatePasswordRequest
import com.example.tbptb.data.UpdateResponse
import com.example.tbptb.network.ApiService
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    authApi: ApiService,
    context: Context
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Fungsi untuk mengambil token dari SharedPreferences
    fun getToken(): String {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return "Bearer ${sharedPreferences.getString("USER_TOKEN", "") ?: ""}"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ubah Password") },
        text = {
            Column {
                TextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Password Baru") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Konfirmasi Password Baru") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Validasi input
                    when {
                        newPassword.isEmpty() -> {
                            Toast.makeText(context, "Password baru tidak boleh kosong", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        newPassword != confirmPassword -> {
                            Toast.makeText(context, "Konfirmasi password tidak sesuai", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        newPassword.length < 6 -> {
                            Toast.makeText(context, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                    }

                    // Lakukan update password
                    isLoading = true
                    val token = getToken()
                    val request = UpdatePasswordRequest(password = newPassword)

                    authApi.updatePassword(token, request).enqueue(object : Callback<UpdateResponse> {
                        override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                            isLoading = false
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                                onDismiss()
                            } else {
                                // Parse error message
                                val errorMessage = try {
                                    val errorBody = response.errorBody()?.string()
                                    val errorJson = JSONObject(errorBody)
                                    errorJson.getString("message")
                                } catch (e: Exception) {
                                    "Gagal mengubah password"
                                }
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                            isLoading = false
                            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                },
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Simpan")
                }
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Batal")
            }
        }
    )
}