package com.example.tbptb

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollaboratorScreen(navController: NavController) {
    val emailList = remember { mutableStateListOf<String>() } // Daftar email collaborator
    val invitationsList = remember { mutableStateListOf<String>() } // Daftar undangan yang masuk

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Collaborator") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tombol untuk menampilkan daftar undangan
            Button(
                onClick = {
                    // Tampilkan daftar undangan
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF469C8F))
            ) {
                Text("Undangan", color = Color.White)
            }

            // Tombol untuk menampilkan form tambah collaborator
            Button(
                onClick = {
                    // Navigasi ke halaman AddCollaboratorScreen
                    navController.navigate("addCollaboratorScreen")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF469C8F))
            ) {
                Text("Tambah Collaborator", color = Color.White)
            }



            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(invitationsList) { invitation ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = invitation,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Button(
                                onClick = {
                                    invitationsList.remove(invitation)
                                    emailList.add(invitation)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF469C8F))
                            ) {
                                Text("Approve", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
