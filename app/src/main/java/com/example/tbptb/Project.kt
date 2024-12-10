package com.example.tbptb

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController



@Composable
fun ProjectScreen(navController: NavController, projects: MutableList<Project>) {
    var showEditDialog by remember { mutableStateOf(false) }
    var projectToEdit by remember { mutableStateOf<Project?>(null) }

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

        // Menampilkan daftar proyek
        projects.forEachIndexed { index, project ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (index % 2 == 0) Color(0xFFDFFBE9) else Color(0xFF5AB19F),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = project.name,
                            style = TextStyle(fontSize = 20.sp, color = Color.Black)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Kolaborator: ${project.collaborator}",
                            style = TextStyle(fontSize = 14.sp, color = Color.Black)
                        )
                        Text(
                            text = project.description.take(50) + if (project.description.length > 50) "..." else "",
                            style = TextStyle(fontSize = 14.sp, color = Color.Black)
                        )
                    }
                    Row {
                        IconButton(onClick = {
                            projectToEdit = project
                            showEditDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color(0xFF4CAF50) // Hijau untuk edit
                            )
                        }
                        IconButton(onClick = { projects.remove(project) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color(0xFFE53935) // Merah untuk hapus
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
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

    // Dialog Edit Project
    if (showEditDialog) {
        projectToEdit?.let { project ->
            EditProjectDialog(
                project = project,
                onDismiss = { showEditDialog = false },
                onSave = { updatedName, updatedDescription ->
                    project.name = updatedName
                    project.description = updatedDescription
                    showEditDialog = false
                }
            )
        }
    }
}

@Composable
fun EditProjectDialog(
    project: Project,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var newName by remember { mutableStateOf(project.name) }
    var newDescription by remember { mutableStateOf(project.description) }

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
