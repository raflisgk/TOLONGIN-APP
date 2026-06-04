package com.example.tolongin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember // Tambahan import untuk remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext // 1. PERBAIKAN: IMPORT LOCALCONTEXT UNTUK SHAREDPREFERENCES
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PembayaranScreen(
    navController: NavController,
    namaLayanan: String = "Pembersihan Rumah",
    totalHarga: String = "Rp 250.000",
    tanggal: String = "",
    alamatDiterima: String = "",
    waktu: String = ""
) {
    // ====================================================================
    // 2. KODE BARU: DEKLARASI CONTEXT & GENERATE ID TRANSAKSI ACAK (TRX)
    // ====================================================================
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("TolonginPref", android.content.Context.MODE_PRIVATE)

    // Membuat angka acak 5 digit digabung dengan prefix TRX (Contoh: TRX57432)
    val randomTrxId = remember { "TRX" + (10000..99999).random().toString() }
    // ====================================================================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // --- TOP BAR (RINGKASAN PESANAN) ---
            Surface(
                modifier = Modifier.fillMaxWidth().shadow(2.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color(0xff212d51))
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Ringkasan Pesanan",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xff212d51))
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // --- KARTU WAKTU & LOKASI ---
                Surface(
                    modifier = Modifier.fillMaxWidth().shadow(30.dp, RoundedCornerShape(32.dp)),
                    shape = RoundedCornerShape(32.dp),
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        // Waktu
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(48.dp).background(Color(0xff64a1ff).copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Schedule, null, tint = Color(0xff005ab2))
                            }
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text("WAKTU LAYANAN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xff4e5a81))
                                Text(
                                    tanggal,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xff212d51)
                                )

                                Text(
                                    when(waktu) {
                                        "Pagi" -> "08:00 - 11:00 WIB"
                                        "Siang" -> "12:00 - 15:00 WIB"
                                        else -> "16:00 - 19:00 WIB"
                                    },
                                    fontSize = 14.sp,
                                    color = Color(0xff4e5a81)
                                )
                            }
                        }

                        HorizontalDivider(color = Color(0xffe3e7ff))

                        // Lokasi
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(48.dp).background(Color(0xffc4d0ff).copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.LocationOn, null, tint = Color(0xff2f55b7))
                            }
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text("LOKASI", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xff4e5a81))
                                Text(
                                    alamatDiterima,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xff212d51)
                                )

                                Text(
                                    "Alamat layanan pelanggan",
                                    fontSize = 14.sp,
                                    color = Color(0xff4e5a81)
                                )
                            }
                        }
                    }
                }

                // --- KARTU RINCIAN PEMBAYARAN ---
                Surface(
                    modifier = Modifier.fillMaxWidth().shadow(30.dp, RoundedCornerShape(32.dp)),
                    shape = RoundedCornerShape(32.dp),
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Rincian Pembayaran", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xff212d51))
                        Spacer(Modifier.height(20.dp))

                        RincianBaris("Harga Jasa (4 Jam)", "Rp 350.000")
                        RincianBaris("Peralatan Pembersihan", "Rp 50.000")
                        RincianBaris("Biaya Aplikasi", "Rp 5.000", isBlue = true)

                        Spacer(Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xffe3e7ff))
                        Spacer(Modifier.height(16.dp))

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Pembayaran", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xff212d51))
                            Text(
                                totalHarga,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xff005ab2)
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        // Promo Button Style
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xffc4d0ff).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color(0xffc4d0ff).copy(alpha = 0.3f))
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, null, tint = Color(0xff2f55b7), modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text("Pakai Kode Promo", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xff2f55b7))
                                    Text("HEMAT LEBIH BANYAK", fontSize = 10.sp, color = Color(0xff1240a2).copy(alpha = 0.6f))
                                }
                            }
                        }
                    }
                }

                // --- METODE PEMBAYARAN ---
                Surface(
                    modifier = Modifier.fillMaxWidth().shadow(10.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xffeff0ff)
                ) {
                    Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(40.dp).background(Color(0xff64a1ff), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AccountBalanceWallet, null, tint = Color.White)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text("METODE BAYAR", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xff005ab2))
                            Text("QRIS", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xff212d51))
                        }
                    }
                }

                // --- BUTTON & TERMS ---
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    // 3. PERBAIKAN: TOMBOL SEKARANG MENYIMPAN ID TRANSAKSI DAN BERPINDAH SECARA DINAMIS
                    Button(
                        onClick = {
                            // Simpan ID Transaksi acak yang baru saja dibuat ke memori HP
                            sharedPreferences.edit().putString("TRX_ID", randomTrxId).apply()

                            // Navigasi ke rute QRIS membawa parameter ID acak dan total harga aktual
                            navController.navigate("qris/$randomTrxId/$totalHarga")
                        },
                        modifier = Modifier.fillMaxWidth().height(68.dp).shadow(6.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff005ab2))
                    ) {
                        Text("Konfirmasi & Bayar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        textAlign = TextAlign.Center,
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = Color(0xff4e5a81), fontSize = 10.sp)) { append("DENGAN MEMBAYAR, ANDA MENYETUJUI\n") }
                            withStyle(SpanStyle(color = Color(0xff005ab2), fontSize = 10.sp, fontWeight = FontWeight.Bold)) { append("SYARAT & KETENTUAN") }
                            withStyle(SpanStyle(color = Color(0xff4e5a81), fontSize = 10.sp)) { append(" KAMI.") }
                        }
                    )
                }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun RincianBaris(label: String, harga: String, isBlue: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = Color(0xff4e5a81))
        Text(harga, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isBlue) Color(0xff2f55b7) else Color(0xff212d51))
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 1000)
@Composable
fun PembayaranPreview() {
    PembayaranScreen(navController = rememberNavController())
}