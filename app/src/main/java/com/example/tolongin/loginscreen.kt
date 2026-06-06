package com.example.tolongin

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF64A1FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.btn_star_big_on),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Selamat datang\nkembali",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E2D5A),
            textAlign = TextAlign.Center,
            lineHeight = 38.sp
        )

        Text(
            text = "Masuk ke akun Tolong.In Anda",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Input Email
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("ALAMAT EMAIL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4E5A81))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Masukkan alamat email", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = { Icon(Icons.Default.Email, null, tint = Color(0xFF4E5A81)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFEEF2FF),
                    unfocusedContainerColor = Color(0xFFEEF2FF),
                    focusedBorderColor = Color(0xFF2563EB),
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Input Password
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("KATA SANDI", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4E5A81))
                Text("Lupa kata sandi?", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB), modifier = Modifier.clickable { })
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("••••••••", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color(0xFF4E5A81)) },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle Password", tint = Color(0xFF4E5A81))
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFEEF2FF),
                    unfocusedContainerColor = Color(0xFFEEF2FF),
                    focusedBorderColor = Color(0xFF2563EB),
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- TOMBOL MASUK DENGAN VALIDASI ROLE DINAMIS LARAGON ---
        Button(
            onClick = {
                val emailBersih = email.lowercase().trim() // Amankan dari spasi & huruf besar otomatis HP
                val pwBersih = password.trim()

                if (emailBersih.isEmpty() || pwBersih.isEmpty()) {
                    Toast.makeText(context, "Email dan Password harus diisi!", Toast.LENGTH_SHORT).show()
                } else {
                    // Semua akun sekarang langsung menembak ke database MySQL Laragon Anda
                    RetrofitClient.instance.loginUser(emailBersih, pwBersih).enqueue(object : Callback<ResponseModel> {
                        override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                            if (response.isSuccessful) {
                                val res = response.body()
                                if (res?.status == "success") {

                                    // Simpan sesi email ke memori HP
                                    val sharedPreferences = context.getSharedPreferences("TolonginPref", Context.MODE_PRIVATE)
                                    sharedPreferences.edit().putString("USER_EMAIL", emailBersih).apply()

                                    // ====================================================================
                                    // PERBAIKAN: SELEKSI ROLE OTOMATIS BERDASARKAN HASIL DATA BASE
                                    // ====================================================================
                                    if (res.role == "helper") {
                                        Toast.makeText(context, "Selamat datang Helper!", Toast.LENGTH_SHORT).show()
                                        navController.navigate("beranda_helper") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    } else {
                                        Toast.makeText(context, res.message, Toast.LENGTH_SHORT).show()
                                        navController.navigate("layarlokasi") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                    // ====================================================================

                                } else {
                                    Toast.makeText(context, res?.message ?: "Gagal Masuk", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                            Toast.makeText(context, "Koneksi Gagal: ${t.message}", Toast.LENGTH_LONG).show()
                        }
                    })
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005AB2))
        ) {
            Text("Masuk", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFEEEEEE))
            Text("  ATAU LANJUT DENGAN  ", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFEEEEEE))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { navController.navigate("google") },
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFDDE1F0)),
            color = Color(0xFFF8F9FF)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Google",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E2D5A)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row {
            Text("Belum punya akun? ", color = Color.Gray)
            Text(
                text = "Daftar",
                color = Color(0xFF005AB2),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate("daftar")
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 800)
@Composable
fun LoginPreview() {
    MaterialTheme {
        LoginScreen(navController = rememberNavController())
    }
}