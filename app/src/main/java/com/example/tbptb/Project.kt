package com.example.tbptb

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tbptb.data.ProjectData
import com.example.tbptb.data.ProjectsResponse
import com.example.tbptb.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun ProjectScreen(
    navController: NavController,
    authApi: ApiService,
    context: Context
) {
    var projects by remember { mutableStateOf<List<ProjectData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fungsi untuk mengambil token dari SharedPreferences
    fun getToken(): String? {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("USER_TOKEN", null)
    }

    // Fungsi untuk fetch projects
    fun fetchProjects() {
        val token = getToken()
        if (token.isNullOrBlank()) {
            Toast.makeText(context, "Authentication required", Toast.LENGTH_SHORT).show()
            navController.navigate("login")
            return
        }

        authApi.getAllProjects("Bearer $token").enqueue(object : Callback<ProjectsResponse> {
            override fun onResponse(call: Call<ProjectsResponse>, response: Response<ProjectsResponse>) {
                isLoading = false
                if (response.isSuccessful) {
                    // Tambahkan null safety
                    projects = response.body()?.data ?: emptyList()
                } else {
                    // Tambahkan detail error dari response code
                    errorMessage = "Failed to fetch projects: ${response.code()} - ${response.message()}"
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProjectsResponse>, t: Throwable) {
                isLoading = false
                errorMessage = "Network error: ${t.localizedMessage ?: "Unknown error"}"
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Trigger fetch saat komponen pertama kali dimuat
    LaunchedEffect(Unit) {
        fetchProjects()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "UnandResearch",
            style = TextStyle(fontSize = 24.sp),
            color = Color.Black
        )
        Text(
            text = "Projects",
            style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tambahkan loading indicator
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else if (errorMessage != null) {
            Text(
                text = errorMessage ?: "Unknown error",
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else if (projects.isEmpty()) {
            Text(
                text = "No projects found",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            // Menampilkan daftar proyek
            // Menampilkan daftar proyek
            LazyColumn {
                items(projects.size) { index ->
                    val project = projects[index]
                    ProjectItemCard(
                        project = project,
                        onEditClick = {
                            // Tambahkan logika edit
                            // Misalnya, buka dialog edit atau navigate ke layar edit
                        },
                        onDeleteClick = {
                            // Tambahkan logika delete
                            // Misalnya, tampilkan konfirmasi atau panggil API delete
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Button untuk menambah proyek
        Button(
            onClick = { navController.navigate("Buat_Project") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5AB19F))
        ) {
            Text("Buat Project Baru", color = Color.White)
        }
    }
}

// Komponen untuk item proyek
@Composable
fun ProjectItemCard(
    project: ProjectData, // Pastikan tipe ProjectData cocok
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFDFFBE9),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = project.nama_project ?: "Unnamed Project", // Tambahkan null safety
                    style = TextStyle(fontSize = 20.sp, color = Color.Black)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Objek: ${project.`object` ?: "N/A"}", // Ganti single quotes dengan double quotes, tambah null safety
                    style = TextStyle(fontSize = 14.sp, color = Color.Black)
                )
                Text(
                    text = (project.deskripsi ?: "").let { desc ->
                        if (desc.length > 50) desc.take(50) + "..."
                        else desc
                    },
                    style = TextStyle(fontSize = 14.sp, color = Color.Black)
                )
            }
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color(0xFF4CAF50)
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFE53935)
                    )
                }
            }
        }
    }
}
@Composable
fun EditProjectDialog(
    project: ProjectData,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var newName by remember { mutableStateOf(project.nama_project.orEmpty()) }
    var newDescription by remember { mutableStateOf(project.deskripsi.orEmpty()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Project") },
        text = {
            Column {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Nama Project") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newDescription,
                    onValueChange = { newDescription = it },
                    label = { Text("Deskripsi Project") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(newName, newDescription) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5AB19F))
            ) {
                Text("Simpan", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = Color(0xFFE53935))
            }
        }
    )
}