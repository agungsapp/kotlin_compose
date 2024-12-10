package com.example.tbptb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ProposalForm(navController: NavController) { // Tambahkan tipe parameter
    var judulProposal by remember { mutableStateOf("") }
    var namaPengaju by remember { mutableStateOf("") }
    var kolaborator1 by remember { mutableStateOf("") }
    var kolaborator2 by remember { mutableStateOf("") }
    var selectedFile by remember { mutableStateOf("No file selected") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Ajukan Proposal", fontSize = 24.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = judulProposal,
            onValueChange = { newValue: String -> judulProposal = newValue },
            label = { Text("Judul Proposal") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = namaPengaju,
            onValueChange = { newValue: String -> namaPengaju = newValue },
            label = { Text("Nama Pengaju") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = kolaborator1,
            onValueChange = { newValue: String -> kolaborator1 = newValue },
            label = { Text("Kolaborator 1") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = kolaborator2,
            onValueChange = { newValue: String -> kolaborator2 = newValue },
            label = { Text("Kolaborator 2") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                selectedFile = "DummyFile.pdf" // Simulate file selection
            },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Text(text = "Pilih File")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = selectedFile)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Navigasi atau logika submit bisa diletakkan di sini
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ajukan Proposal")
        }
    }
}
