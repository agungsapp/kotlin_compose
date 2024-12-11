@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tbptb

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tbptb.ui.theme.TBPTBTheme
import kotlinx.coroutines.delay
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.clickable
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.border
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import com.example.tbptb.data.AuthRequest
import com.example.tbptb.data.AuthResponse
import com.example.tbptb.network.ApiClient
import com.example.tbptb.network.ApiService
import android.util.Log
import com.example.tbptb.ui.theme.UpdateProfile
import com.example.tbptb.viewmodel.LoginViewModel
import com.example.tbptb.viewmodel.SignupViewModel
import retrofit2.Call
import retrofit2.Callback
import androidx.compose.ui.unit.sp
import retrofit2.Response
import androidx.lifecycle.viewmodel.compose.viewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import android.content.SharedPreferences
import com.example.tbptb.data.RegisterRequest
import androidx.compose.ui.text.TextStyle



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TBPTBTheme {
                val navController = rememberNavController()
                val projects = remember { mutableStateListOf<Project>() }
                val authApi = ApiClient.retrofit.create(ApiService::class.java)
                val context = LocalContext.current

                // Setup NavHost untuk menavigasi antar layar
                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") { SplashScreen(navController) }
                    composable("main") { MainContent(navController) }
                    composable("login") {
                        // Menambahkan ViewModel untuk LoginScreen
                        val loginViewModel: LoginViewModel = viewModel()
                        LoginScreen(navController = navController, loginViewModel = loginViewModel)
                    }
                    composable("signup") {
                        val signupViewModel: SignupViewModel = viewModel()  // Perhatikan nama variabel ini
                        SignUpScreen(navController = navController, viewModel = signupViewModel)
                    }
                    composable("dashboard") { DashboardScreen(navController) }
                    composable("add_task") { AddTaskScreen(navController) }
                    composable("Collaborator") { CollaboratorScreen(navController) }
                    composable("profile") { ProfileScreen(
                        navController = navController,
                        authApi = authApi,
                        context = context
                    ) }
                    composable("update_profile") { UpdateProfile() }
                    composable("ProjectScreen") {
                        ProjectScreen(
                            navController = navController,
                            authApi = authApi,
                            context = context
                        )
                    }
                    composable("Buat_Project") {
                        ProjectCreationScreen(
                            navController = navController,
                            addProject = { newProject ->
                                projects.add(newProject) // Menambahkan proyek baru ke daftar
                            }
                        )
                    }

                    // Menambahkan navigasi ke AddCollaboratorScreen
                    composable("addCollaboratorScreen") {
                        AddCollaboratorScreen(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("main") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF469C8F)),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = R.drawable.logo_ur_app), contentDescription = "Logo")
    }
}

@Composable
fun MainContent(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF469C8F))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.buku),
                contentDescription = "Book Image",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = "Organize your scientific research \nwith Unand Research APP!",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Register your progress with accuracy.\n" +
                        "Track and share deadline, notes, and tasks with your \n" +
                        "colleagues and advisors.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))


            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Login", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button Sign Up
            TextButton(onClick = { navController.navigate("signup") }) {
                Text("Belum punya akun? Daftar", color = Color.White)
            }
        }
    }
}


@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    // Tambahkan konteks untuk Toast
    val context = LocalContext.current

    // Buat instance AuthApi
    val authApi = ApiClient.retrofit.create(ApiService::class.java)
    fun saveToken(context: Context, token: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("USER_TOKEN", token) // Menyimpan token
        editor.apply() // Menyimpan perubahan
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF469C8F)) // Warna hijau untuk bg
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo aplikasi
            Spacer(modifier = Modifier.height(40.dp))
            Image(
                painter = painterResource(id = R.drawable.logo_ur_app),
                contentDescription = "Logo Unand Research",
                modifier = Modifier
                    .size(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Input Email
                    TextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text("Email") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Gray,
                            unfocusedIndicatorColor = Color.LightGray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Input Password
                    TextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text("Password") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent),
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Gray,
                            unfocusedIndicatorColor = Color.LightGray
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Tombol "Login"
                    Button(
                        onClick = {
                            if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
                                val request = AuthRequest(email.value, password.value)
                                authApi.login(request).enqueue(object : Callback<AuthResponse> {
                                    override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                                        if (response.isSuccessful) {
                                            val body = response.body()
                                            if (body?.message == "User logged in successfully" && body.data?.token != null) {
                                                val token = body.data.token
                                                saveToken(context, token)
                                                navController.navigate("Dashboard")
                                                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                                            } else {
                                                // Jika token null atau message tidak sesuai
                                                Toast.makeText(context, "Login Failed: ${body?.message ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            // Jika respons tidak berhasil
                                            Toast.makeText(context, "Login Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                                        Toast.makeText(context, "Login Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            } else {
                                Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp), // Tinggi tombol
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF469C8F))
                    ) {
                        Text("Login", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tautan ke halaman Signup
                    Text(
                        text = "Don't have an account? Sign up",
                        modifier = Modifier.clickable {
                            navController.navigate("signup")
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }
            }
        }
    }
}


@Composable
fun SignUpScreen(navController: NavController, viewModel: SignupViewModel) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) } // Untuk menampilkan indikator loading
    val errorMessage = remember { mutableStateOf("") } // Untuk menampilkan pesan error
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF469C8F)) // Warna hijau untuk latar belakang penuh
    ) {
        // Bagian putih dengan sudut membulat di bawah
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.80f) // Mengisi 80% dari tinggi layar
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .align(Alignment.TopCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Input Name
                TextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent, // Latar belakang transparan
                        focusedIndicatorColor = Color.Gray, // Warna garis saat fokus
                        unfocusedIndicatorColor = Color.LightGray // Warna garis saat tidak fokus
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Input Email
                TextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.LightGray
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Input Password
                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.LightGray
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Input Confirm Password
                TextField(
                    value = confirmPassword.value,
                    onValueChange = { confirmPassword.value = it },
                    label = { Text("Confirm Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.LightGray
                    )
                )

                // Error message jika password tidak cocok
                if (errorMessage.value.isNotEmpty()) {
                    Text(
                        text = errorMessage.value,
                        color = Color.Red,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Red
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(120.dp))

                // Tombol "Create Account"
                Button(
                    onClick = {
                        if (password.value == confirmPassword.value) {
                            val registerRequest = RegisterRequest(name = name.value, email = email.value, password = password.value)
                            isLoading.value = true
                            viewModel.register(registerRequest, navController, context) // Kirimkan registerRequest di sini
                        } else {
                            errorMessage.value = "Passwords do not match."
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp), // Tinggi tombol sesuai desain
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF469C8F))
                ) {
                    if (isLoading.value) {
                        CircularProgressIndicator(color = Color.White) // Indikator loading
                    } else {
                        Text("Create Account", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FA))
            .padding(16.dp)
    ) {
        // Header Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.Gray)
                        .clickable { navController.navigate("profile") }, // Navigasi ke halaman profil
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile Icon",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text("Welcome Back!", style = MaterialTheme.typography.bodyMedium)
                    Text("Hallo, Rahma!", style = MaterialTheme.typography.headlineSmall)
                }
            }

            IconButton(onClick = { /* Handle notification click */ }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification Icon"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Calendar Section
        Text("Oktober 2024", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DateButton("Fri", "11")
            DateButton("Sat", "12")
            DateButton("Sun", "13", isSelected = true)
            DateButton("Mon", "14")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("Buat_Project")},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF469C8F))
        ) {
            Text("New Project")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Current Project", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProjectCard(
                title = "Rancangan Aplikasi Umroh",
                members = listOf("member1.jpg", "member2.jpg"),
                progress = 51
            )
            ProjectCard(
                title = "Sistem Informasi Absensi",
                members = listOf("member1.jpg", "member2.jpg", "member3.jpg"),
                progress = 51
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Your Project", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        ProjectDetailsCard(
            title = "Perancangan Aplikasi Umroh",
            status = "On Progress",
            objectName = "PT Cahaya Hati Cabang Padang",
            collaborators = listOf("Radatul Mutmainnah", "Regina Nathamiya")
        )

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Navigation with Custom Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(15.dp)) // Radius 15
                .background(Color.White) // Background putih
                .border(
                    width = 2.dp,
                    color = Color(0xFF469C8F), // Stroke hijau
                    shape = RoundedCornerShape(15.dp)
                )
        ) {
            NavigationBar(
                containerColor = Color.Transparent, // Hindari warna latar default
                tonalElevation = 0.dp // Hindari efek elevasi default
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Group, contentDescription = "Collaborator", tint = Color(0xFF469C8F)) },
                    label = { Text("Collaborator", color = Color(0xFF469C8F)) },
                    selected = false,
                    onClick = { navController.navigate("Collaborator") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Check, contentDescription = "Task", tint = Color(0xFF469C8F)) },
                    label = { Text("Task", color = Color(0xFF469C8F)) },
                    selected = false,
                    onClick = { navController.navigate("add_task") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Folder, contentDescription = "Project", tint = Color(0xFF469C8F)) },
                    label = { Text("Project", color = Color(0xFF469C8F)) },
                    selected = true,
                    onClick = { navController.navigate("ProjectScreen") }
                )
            }
        }
    }
}


@Composable
fun DateButton(day: String, date: String, isSelected: Boolean = false) {
    Column(
        modifier = Modifier
            .background(
                if (isSelected) Color(0xFFBFD4E5) else Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(day, style = MaterialTheme.typography.bodySmall)
        Text(date, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun ProjectCard(title: String, members: List<String>, progress: Int) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(120.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF469C8F))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                members.forEach { member ->
                    // Replace with Image composable
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Member",
                        tint = Color.White
                    )

                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress / 100f,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            )
            Text("$progress% completed", style = MaterialTheme.typography.bodySmall, color = Color.White)
        }
    }
}

@Composable
fun ProjectDetailsCard(
    title: String,
    status: String,
    objectName: String,
    collaborators: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF469C8F))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = Color.White)
            Text("Status: $status", style = MaterialTheme.typography.bodySmall, color = Color.White)
            Text("Objek: $objectName", style = MaterialTheme.typography.bodySmall, color = Color.White)
            Text("Collaborator: ${collaborators.joinToString()}", style = MaterialTheme.typography.bodySmall, color = Color.White)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    TBPTBTheme {
        MainContent(navController = rememberNavController())
    }
}


@Composable
fun MyApp(navController: NavController) {
    val loginViewModel: LoginViewModel = viewModel()
    LoginScreen(navController = navController, loginViewModel = loginViewModel)
}

