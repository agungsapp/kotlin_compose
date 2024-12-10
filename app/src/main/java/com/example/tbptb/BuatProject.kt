package com.example.tbptb

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class Project(
    var name: String,
    var description: String,
    val collaborator: String
)

@Composable
fun ProjectCreationScreen(navController: NavController, addProject: (Project) -> Unit) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var collaborator by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Buat Project Penelitian", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp))

        // Input Fields untuk Nama, Deskripsi, dan Email Kolaborator
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama Project") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Deskripsi Project") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = collaborator,
            onValueChange = { collaborator = it },
            label = { Text("Email Kolaborator") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Menambahkan proyek baru dan kembali ke layar sebelumnya
                val newProject = Project(name, description, collaborator)
                addProject(newProject) // Menambahkan proyek baru ke state di MainActivity
                Toast.makeText(context, "Project telah dibuat!", Toast.LENGTH_SHORT).show()
                navController.popBackStack() // Kembali ke layar sebelumnya (ProjectScreen)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buat Project")
        }
    }
}
