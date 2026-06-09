package com.example.tolongin.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tolongin.PesananModel
import com.example.tolongin.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class RiwayatTarikModel(val id: String, val tanggal: String, val nominal: Int)

@Composable
fun LaporanMitraScreen(navController: NavHostController) {
    val context   = LocalContext.current
    val isPreview = LocalInspectionMode.current

    val DBiruTua  = Color(0xFF004387)
    val DBiruMuda = Color(0xFF93BAFD)
    val DBgPage   = Color(0xFFF7F8FC)
    val DBgChart  = Color(0xFFF2F3FB)

    val sharedPref = context.getSharedPreferences("LaporanPrefs", android.content.Context.MODE_PRIVATE)

    var listPesananDb by remember { mutableStateOf<List<PesananModel>>(emptyList()) }

    // Baca riwayat dari SharedPreferences agar tidak hilang saat pindah layar
    var riwayatPenarikan by remember {
        val json = sharedPref.getString("riwayat_tarik", "") ?: ""
        val list = if (json.isEmpty()) emptyList()
        else json.split("|").mapNotNull { item ->
            val parts = item.split(";")
            if (parts.size == 3) RiwayatTarikModel(parts[0], parts[1], parts[2].toIntOrNull() ?: 0)
            else null
        }
        mutableStateOf(list)
    }

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (!isPreview) {
            RetrofitClient.instance.getPesanan("raflisgk@gmail.com").enqueue(object : Callback<List<PesananModel>> {
                override fun onResponse(call: Call<List<PesananModel>>, response: Response<List<PesananModel>>) {
                    isLoading = false
                    if (response.isSuccessful) listPesananDb = response.body() ?: emptyList()
                }
                override fun onFailure(call: Call<List<PesananModel>>, t: Throwable) {
                    isLoading = false
                    Toast.makeText(context, "Gagal memuat: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            isLoading = false
        }
    }

    val listSelesai          = listPesananDb.filter { it.status.equals("Selesai", ignoreCase = true) }
    val jumlahPesananSelesai = if (isPreview) 42 else listSelesai.size
    val totalPendapatanKotor = listSelesai.sumOf { it.total_harga.filter { c -> c.isDigit() }.toIntOrNull() ?: 0 }
    val totalSudahDitarik    = riwayatPenarikan.sumOf { it.nominal }
    val saldoAktifTersedia   = if (isPreview) 1250000 else (totalPendapatanKotor - totalSudahDitarik)
    val formatRupiah         = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    Scaffold(
        containerColor = DBgPage,
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White)
                    .padding(24.dp).statusBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tolong.in", color = DBiruTua, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                Icon(Icons.Default.Notifications, contentDescription = "Notif", tint = Color.Gray)
            }
        },
        bottomBar = { CustomBottomNavMitra(navController = navController, tabAktif = "Laporan") }
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(top = 20.dp, bottom = 30.dp)
            ) {

                // ── HEADER + TOMBOL TARIK SALDO ──
                item {
                    Column {
                        Text("Laporan Keuangan", color = Color(0xFF191C21), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Ringkasan pendapatan dan performa Anda.", color = Color(0xFF424752), fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (saldoAktifTersedia > 0) {
                                    val tgl = SimpleDateFormat("dd MMM yyyy • HH:mm", Locale("id", "ID")).format(Date())
                                    val baru = RiwayatTarikModel("WD-${System.currentTimeMillis()}", tgl, saldoAktifTersedia)
                                    val listBaru = listOf(baru) + riwayatPenarikan
                                    riwayatPenarikan = listBaru
                                    // Simpan ke SharedPreferences
                                    val jsonBaru = listBaru.joinToString("|") { "${it.id};${it.tanggal};${it.nominal}" }
                                    sharedPref.edit().putString("riwayat_tarik", jsonBaru).apply()
                                    Toast.makeText(context, "Berhasil ditarik!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DBiruTua),
                            shape = RoundedCornerShape(99.dp)
                        ) {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tarik Saldo")
                        }
                    }
                }

                // ── KARTU SALDO ──
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .shadow(16.dp, RoundedCornerShape(24.dp))
                            .clip(RoundedCornerShape(24.dp))
                            .background(DBiruTua)
                            .padding(24.dp)
                    ) {
                        Column {
                            Text("Total Saldo Aktif", color = Color(0xFFD6E3FF), fontSize = 14.sp)
                            Text(
                                formatRupiah.format(saldoAktifTersedia).replace(",00", ""),
                                color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                // ── STATS ──
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        GridStatBox(modifier = Modifier.weight(1f), icon = "✔️", label = "Pesanan", value = jumlahPesananSelesai.toString())
                        GridStatBox(modifier = Modifier.weight(1f), icon = "⭐", label = "Rating",  value = "4.8")
                    }
                }

                // ── TREN PENDAPATAN ──
                item {
                    Text("Tren Pendapatan", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().height(220.dp)
                            .clip(RoundedCornerShape(20.dp)).background(DBgChart).padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            val heights = listOf(0.4f, 0.5f, 0.3f, 1.0f, 0.45f, 0.6f)
                            heights.forEachIndexed { i, h ->
                                Box(
                                    modifier = Modifier.width(36.dp).fillMaxHeight(h * 0.7f)
                                        .background(
                                            if (i == 3) DBiruTua else DBiruMuda,
                                            RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                                        )
                                )
                            }
                        }
                    }
                }

                // ── RIWAYAT PENARIKAN ──
                item {
                    Text("Riwayat Transaksi", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    if (riwayatPenarikan.isEmpty()) {
                        Text("Belum ada riwayat penarikan.", color = Color.Gray, fontSize = 13.sp)
                    } else {
                        riwayatPenarikan.forEach { r ->
                            RiwayatItemUI(
                                icon    = "💸",
                                judul   = "Penarikan Saldo",
                                tanggal = r.tanggal,
                                nominal = "- ${formatRupiah.format(r.nominal).replace(",00", "")}"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GridStatBox(modifier: Modifier, icon: String, label: String, value: String) {
    Row(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Text("$icon $label", color = Color.Gray, fontSize = 12.sp)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RiwayatItemUI(icon: String, judul: String, tanggal: String, nominal: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            .background(Color.White, RoundedCornerShape(12.dp)).padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFF2F3FB)),
                contentAlignment = Alignment.Center
            ) { Text(icon) }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(judul, fontWeight = FontWeight.Bold)
                Text(tanggal, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Text(nominal, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
    }
}