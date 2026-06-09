package com.example.tolongin.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

// ── Definisi Warna Sesuai Mockup ─────────────────────────────────────────────
private val DPrimary      = Color(0xFF005AB2)
private val DPrimaryDark  = Color(0xFF0D3585)
private val DBgPage       = Color(0xFFF7F8FC)
private val DTextDark     = Color(0xFF111827)
private val DTextMid      = Color(0xFF4B5563)
private val DTextLight    = Color(0xFF9CA3AF)
private val DBorderCol    = Color(0xFFE5E7EB)

data class LogRiwayat(
    val waktu: String,
    val judul: String,
    val subJudul: String
)

@Composable
fun DetailPesananMitraScreen(
    navController: NavHostController,
    idTransaksi: String,
    statusAwal: String
) {
    val context = LocalContext.current

    // ── INDEX STEP AKSI (0: Terima, 1: Menuju, 2: Sampai, 3: Kerja, 4: Selesai)
    var stepIndex by remember { mutableStateOf(0) }
    val stepsList = listOf("Terima", "Menuju", "Sampai", "Kerja", "Hampir", "Selesai")

    // State teks dinamis status utama atas
    val statusUtamaText = when (stepIndex) {
        0 -> "Pesanan Diterima"
        1 -> "Menuju Lokasi Pelanggan"
        2 -> "Sudah Sampai di Lokasi"
        3 -> "Sedang Dikerjakan"
        else -> "Pesanan Selesai"
    }

    // State teks dinamis tombol bawah ala Gojek
    val teksTombolBawah = when (stepIndex) {
        0 -> "Mulai Perjalanan"
        1 -> "Saya Sudah Sampai"
        2 -> "Mulai Pekerjaan"
        3 -> "Selesaikan Pesanan"
        else -> "Kembali ke Beranda"
    }

    // List riwayat update yang bertambah otomatis tiap klik tombol
    val riwayatLogs = remember { mutableStateListOf<LogRiwayat>() }

    // Set awal riwayat isi default paket diterima
    if (riwayatLogs.isEmpty()) {
        riwayatLogs.add(LogRiwayat("09:50 WIB", "Pesanan Diterima", "Helper bersiap menuju lokasi."))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DBgPage)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp)
        ) {

            // ── TOP BAR ───────────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .statusBarsPadding()
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF3F4F6))
                            .clickable { navController.popBackStack() }
                    ) {
                        Text("←", color = Color.Black, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
                    }

                    Text(
                        "Detail Pesanan",
                        color = DTextDark,
                        style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // ── HORIZONTAL STEPPER STATUS INDICATOR ────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(statusUtamaText, color = DPrimary, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))
                        Text("Est. Selesai: 14:00", color = DTextMid, style = TextStyle(fontSize = 12.sp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        stepsList.forEachIndexed { index, label ->
                            val isPassed = index <= stepIndex

                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(if (isPassed) DPrimary else Color(0xFFE5E7EB))
                                    .border(2.dp, if (isPassed) DPrimary else Color.Transparent, CircleShape)
                            )

                            if (index < stepsList.lastIndex) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(4.dp)
                                        .background(if (index < stepIndex) DPrimary else Color(0xFFE5E7EB))
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        stepsList.forEachIndexed { index, label ->
                            Text(
                                text = label,
                                color = if (index <= stepIndex) DPrimary else DTextLight,
                                fontSize = 10.sp,
                                fontWeight = if (index == stepIndex) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.width(48.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // ── CARD DETAIL LAYANAN & BIO PELANGGAN ────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = Color(0xFF3A5A9E).copy(alpha = 0.05f))
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text("Perbaikan AC Split Biking", color = DTextDark, style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.ExtraBold))
                            Text("ID Pesanan: $idTransaksi", color = DTextLight, fontSize = 12.sp)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFEE2E2))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Darurat", color = Color(0xFFEF4444), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF9FAFB), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFDDE1F0)), contentAlignment = Alignment.Center) {
                            Text("👨‍🔧", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Bapak Anton Wijaya", color = DTextDark, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Pelanggan Baru", color = DTextLight, fontSize = 11.sp)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFEFF6FF)), contentAlignment = Alignment.Center) { Text("💬", fontSize = 14.sp) }
                            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFEFF6FF)), contentAlignment = Alignment.Center) { Text("📞", fontSize = 14.sp) }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("📍", fontSize = 14.sp)
                        Column {
                            Text("Lokasi Tujuan Pelanggan", color = DTextDark, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Perumahan Indah Asri Blok C4 No. 12, Kec. Kebayoran Baru, Jakarta Selatan", color = DTextMid, fontSize = 12.sp, lineHeight = 1.4.em)
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFEFF6FF), RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Text("Estimasi Biaya (Tunai)", color = DPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        Text("Rp 100.000", color = DPrimary, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }

            // ── LOKASI PETA MAPS ──────────────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Lokasi Pengerjaan", color = DTextDark, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.linearGradient(listOf(Color(0xFF8BBCE5), Color(0xFF6399C7))))
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, DBorderCol, RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .clickable { Toast.makeText(context, "Membuka Google Maps...", Toast.LENGTH_SHORT).show() }
                            .padding(vertical = 12.dp)
                    ) {
                        Text("🗺️ Buka di Maps", color = DPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // ── LIST RIWAYAT LOG UPDATE PROGRESS ──────────────────────────
            item {
                Text(
                    text = "Riwayat Jalur Progress",
                    color = DTextDark,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 20.dp).padding(top = 8.dp)
                )
            }

            items(riwayatLogs) { log ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(16.dp)) {
                        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(DPrimary))
                        Box(modifier = Modifier.width(2.dp).height(50.dp).background(Color(0xFFE5E7EB)))
                    }
                    Column {
                        Text(log.judul, color = DTextDark, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(log.subJudul, color = DTextMid, fontSize = 11.sp)
                        Text(log.waktu, color = DTextLight, fontSize = 10.sp, modifier = Modifier.padding(top = 2.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        // ── SATU TOMBOL AKSIS DINAMIS GOJEK STYLE ─────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .shadow(24.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp), spotColor = Color.Black.copy(alpha = 0.1f))
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color.White)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (stepIndex == 4) Color(0xFF10B981) else DPrimary)
                    .clickable {
                        when (stepIndex) {
                            0 -> {
                                stepIndex = 1
                                riwayatLogs.add(0, LogRiwayat("10:15 WIB", "Menuju Lokasi Pelanggan", "Helper sedang berkendara menggunakan rute tercepat."))
                            }
                            1 -> {
                                stepIndex = 2
                                riwayatLogs.add(0, LogRiwayat("10:30 WIB", "Sampai di Lokasi", "Helper telah tiba di rumah Bpk. Anton."))
                            }
                            2 -> {
                                stepIndex = 3
                                riwayatLogs.add(0, LogRiwayat("10:45 WIB", "Sedang Dikerjakan", "Teknisi sedang melakukan pengecekan freon."))
                            }
                            3 -> {
                                // ── LANGKAH 2: MELEMPAR ID TRANSAKSI ASLI KE HALAMAN LAPORAN ──
                                navController.navigate("laporan_penyelesaian/$idTransaksi")
                            }
                            else -> {
                                navController.navigate("beranda_helper") {
                                    popUpTo("beranda_helper") { inclusive = true }
                                }
                            }
                        }
                    }
                    .padding(vertical = 15.dp)
            ) {
                Text(
                    text = teksTombolBawah,
                    color = Color.White,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

// ── PREVIEW BERDIRI MANDIRI DI LUAR ───────────────────
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun DetailPesananMitraPreview() {
    MaterialTheme {
        val dummyNavController = rememberNavController()
        DetailPesananMitraScreen(
            navController = dummyNavController,
            idTransaksi = "TRX-9824",
            statusAwal = TODO(),
        )
    }
}
