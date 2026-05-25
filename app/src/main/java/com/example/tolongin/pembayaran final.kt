package com.example.tolongin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// TAMBAHAN NO 4: Memasukkan NavController ke dalam parameter fungsi
fun QrisPaymentScreen(
    navController: NavController,
    namaLayanan: String = "Cleaning Plus",
    totalHarga: String = "$149.00",
    idTransaksi: String = "TRX-772910"
) {

    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pembayaran Aman",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        color = Color(0xff212d51)
                    )
                },
                navigationIcon = {
                    // Berfungsi untuk kembali ke layar sebelumnya
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color(0xff0084ff))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 1. KOTAK TOTAL PEMBAYARAN
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xffeff0ff))
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "TOTAL PEMBAYARAN",
                    color = Color(0xff4e5a81),
                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                )
                Text(
                    text = totalHarga,
                    color = Color(0xff212d51),
                    style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Black)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Chip Timer
                Surface(
                    shape = RoundedCornerShape(99.dp),
                    color = Color(0xfffb5151).copy(alpha = 0.1f),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = Color(0xffb31b25), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("14:59", color = Color(0xffb31b25), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 2. KOTAK QR CODE
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .shadow(20.dp, RoundedCornerShape(32.dp))
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.White)
                    .border(BorderStroke(4.dp, Color(0xffe3e7ff)), RoundedCornerShape(32.dp))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color(0xFF333333)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("QRIS", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    Text("Safe Work", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 3. INSTRUKSI PEMBAYARAN
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(99.dp)).background(Color(0xff64a1ff).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xff005ab2), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Buka aplikasi mobile banking atau e-wallet Anda, pindai kode QR, dan selesaikan pembayaran.",
                    color = Color(0xff4e5a81),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. DETAIL TRANSAKSI
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xffe3e7ff).copy(alpha = 0.5f))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Merchant", color = Color(0xff4e5a81), fontSize = 12.sp)
                    Text("Tolong.In Home Services", color = Color(0xff212d51), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("ID Transaksi", color = Color(0xff4e5a81), fontSize = 12.sp)
                    Text("#$idTransaksi", color = Color(0xff212d51), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 5. TOMBOL SAYA SUDAH BAYAR (TAMBAHAN NO 4: Navigasi ke Daftar Pesanan)
            Button(
                onClick = {

                    val apiService = RetrofitClient.instance

                    apiService.simpanPemesanan(
                        idTransaksi,
                        "user@gmail.com",
                        namaLayanan,
                        "24 Mei 2025",
                        "Lunas",
                        totalHarga
                    ).enqueue(object : Callback<ResponseModel> {

                        override fun onResponse(
                            call: Call<ResponseModel>,
                            response: Response<ResponseModel>
                        ) {

                            if (response.isSuccessful) {

                                Toast.makeText(
                                    context,
                                    "Pembayaran berhasil",
                                    Toast.LENGTH_SHORT
                                ).show()

                                navController.navigate("daftar_pesanan")
                            }
                        }

                        override fun onFailure(
                            call: Call<ResponseModel>,
                            t: Throwable
                        ) {

                            Toast.makeText(
                                context,
                                "Gagal: ${t.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .shadow(16.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xff005ab2), Color(0xff64a1ff))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Saya sudah bayar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            TextButton(
                onClick = { navController.popBackStack() }, // Balik ke layar pilih metode pembayaran
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Ubah metode pembayaran", color = Color(0xff005ab2), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 1000)
@Composable
private fun BodyPreview() {
    MaterialTheme {
        QrisPaymentScreen(
            navController = rememberNavController(), // Supaya preview tidak error
            namaLayanan = "Cleaning Plus",
            totalHarga = "$149.00",
            idTransaksi = "TRX-772910"
        )
    }
}