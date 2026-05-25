package com.example.tolongin

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle // <-- INI IMPORT KUNCINYA MAS
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformasiPribadiScreen(navController: NavController) {
    val context = LocalContext.current

    // --- STATE UNTUK MENAMPUNG DATA ---
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telepon by remember { mutableStateOf("") }
    var jenisKelamin by remember { mutableStateOf("") }
    var tanggalLahir by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // --- LOGIKA TARIK DATA DARI DATABASE ---
    LaunchedEffect(Unit) {
        val emailLogin = "raflisgk@gmail.com"

        RetrofitClient.instance.getProfil(emailLogin).enqueue(object : Callback<ProfilResponse> {
            override fun onResponse(call: Call<ProfilResponse>, response: Response<ProfilResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success" && body.data != null) {
                        nama = body.data.nama ?: ""
                        email = body.data.email ?: ""
                        telepon = body.data.telepon ?: ""
                        jenisKelamin = body.data.jenis_kelamin ?: ""
                        tanggalLahir = body.data.tanggal_lahir ?: ""
                        alamat = body.data.alamat ?: ""
                    }
                }
            }
            override fun onFailure(call: Call<ProfilResponse>, t: Throwable) {
                Toast.makeText(context, "Koneksi Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personal Information", fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color(0xFF005AB2))
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    isLoading = true
                    RetrofitClient.instance.updateProfil(nama, email, telepon, jenisKelamin, tanggalLahir, alamat)
                        .enqueue(object : Callback<ResponseModel> {
                            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                                isLoading = false
                                Toast.makeText(context, "Profil Berhasil Diperbarui!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                                isLoading = false
                                Toast.makeText(context, "Gagal Update Database", Toast.LENGTH_SHORT).show()
                            }
                        })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005AB2)),
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Menyimpan..." else "Simpan Perubahan", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // FOTO PROFIL
            Box(modifier = Modifier.size(96.dp).align(Alignment.CenterHorizontally)) {
                Image(
                    painter = painterResource(id = R.drawable.alex_curator),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(24.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("Basic Details", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
            Spacer(modifier = Modifier.height(16.dp))

            ProfileTextField("NAMA LENGKAP", nama, Icons.Default.Person) { nama = it }
            ProfileTextField("EMAIL", email, Icons.Default.Email) { email = it }
            ProfileTextField("NOMOR TELEPON", telepon, Icons.Default.Phone) { telepon = it }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    ProfileTextField("JENIS KELAMIN", jenisKelamin, Icons.Default.Wc) { jenisKelamin = it }
                }
                Box(modifier = Modifier.weight(1f)) {
                    ProfileTextField("TANGGAL LAHIR", tanggalLahir, Icons.Default.CalendarToday) { tanggalLahir = it }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text("Alamat Tersimpan", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = alamat,
                onValueChange = { alamat = it },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFEFF0FF),
                    unfocusedContainerColor = Color(0xFFEFF0FF),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                textStyle = TextStyle(color = Color(0xFF1E2D5A), fontSize = 14.sp)
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun ProfileTextField(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4E5A81), letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(icon, contentDescription = null, tint = Color(0xFF005AB2).copy(alpha = 0.7f)) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFEFF0FF),
                unfocusedContainerColor = Color(0xFFEFF0FF),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            singleLine = true,
            textStyle = TextStyle(color = Color(0xFF1E2D5A), fontSize = 15.sp)
        )
    }
}