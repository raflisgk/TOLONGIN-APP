package com.example.tolongin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
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

@Composable
fun DaftarScreen(navController: NavController) {
    val context = LocalContext.current

    val namaState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val telpState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .systemBarsPadding() // Melindungi dari status bar atas
            .navigationBarsPadding() // Melindungi dari tombol navigasi bawah
            .imePadding() // Biar bisa ke-scroll saat keyboard muncul
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 40.dp, bottom = 40.dp, start = 24.dp, end = 24.dp)
        ) {
            item {
                // --- LOGO ---
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xff005ab2),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tolong.in",
                        color = Color(0xff212d51),
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- JUDUL ---
                Text(
                    text = "Buat Akun",
                    color = Color(0xff212d51),
                    style = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Masukkan detail Anda untuk memulai\nlayanan pembersihan Anda.",
                    color = Color(0xff4e5a81),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // --- FORM INPUT ---
                InputFieldCustom("NAMA LENGKAP", "Gus Miftahul", Icons.Default.Person, state = namaState)
                InputFieldCustom("EMAIL", "contoh@gmail.com", Icons.Default.Email, state = emailState)
                InputFieldCustom("NOMOR TELEPON", "0812-xxxx-xxxx", Icons.Default.Phone, state = telpState)
                InputFieldCustom("PASSWORD", "••••••••", Icons.Default.Lock, isPassword = true, state = passwordState)

                Spacer(modifier = Modifier.height(24.dp))

                // --- TOMBOL BUAT AKUN ---
                Button(
                    onClick = {
                        val nama = namaState.value
                        val email = emailState.value
                        val pass = passwordState.value

                        if (nama.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                            Toast.makeText(context, "Lengkapi Nama, Email, dan Password!", Toast.LENGTH_SHORT).show()
                        } else {
                            RetrofitClient.instance.registerUser(nama, email, pass).enqueue(object : Callback<ResponseModel> {
                                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                                    if (response.isSuccessful) {
                                        val res = response.body()
                                        if (res?.status == "success") {
                                            Toast.makeText(context, "Pendaftaran Berhasil!", Toast.LENGTH_SHORT).show()

                                            // PINDAH KE LAYAR LOKASI
                                            navController.navigate("layarlokasi") {
                                                popUpTo("daftar") { inclusive = true }
                                            }
                                        } else {
                                            Toast.makeText(context, res?.message ?: "Gagal Daftar", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                                    Toast.makeText(context, "Koneksi Gagal: ${t.message}", Toast.LENGTH_LONG).show()
                                }
                            })
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xff005ab2), Color(0xff64a1ff))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Buat Akun", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Sudah punya akun? ", color = Color(0xff4e5a81), fontSize = 16.sp)
                    Text(
                        text = "Masuk",
                        color = Color(0xff005ab2),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { navController.popBackStack() }
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                // --- FOOTER ---
                Text(
                    text = "DENGAN MENDAFTAR, ANDA MENYETUJUI\nSYARAT DAN KETENTUAN SERTA KEBIJAKAN PRIVASI\nTOLONG.IN.",
                    color = Color(0xFF9CA3AF),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                // --- RUANG KOSONG TAMBAHAN DI BAWAH ---
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputFieldCustom(
    label: String,
    placeholder: String,
    icon: ImageVector,
    isPassword: Boolean = false,
    state: MutableState<String>
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xff4e5a81),
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = state.value,
            onValueChange = { state.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xffe8f0ff),
                unfocusedContainerColor = Color(0xffe8f0ff),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Color(0xff005ab2)
            ),
            placeholder = {
                Text(placeholder, color = Color(0xff4e5a81).copy(alpha = 0.5f), fontSize = 15.sp)
            },
            leadingIcon = {
                Icon(imageVector = icon, contentDescription = null, tint = Color(0xff4e5a81).copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
            },
            trailingIcon = if (isPassword) {
                {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = "Toggle Password",
                            tint = Color(0xff4e5a81).copy(0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = true,
            textStyle = TextStyle(fontSize = 15.sp, color = Color(0xff212d51))
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun LayarDaftarPreview() {
    MaterialTheme {
        DaftarScreen(navController = rememberNavController())
    }
}