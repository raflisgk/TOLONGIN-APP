package com.example.tolongin.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

private val Primary      = Color(0xFF1A4FAE)
private val PrimaryDark  = Color(0xFF0D3585)
private val PrimaryLight = Color(0xFF3B6FD4)
private val BgPage       = Color(0xFFFFFFFF)
private val BgCard       = Color(0xFFF7F8FC)
private val TextDark     = Color(0xFF111827)
private val TextMid      = Color(0xFF4B5563)
private val TextLight    = Color(0xFF9CA3AF)
private val GreenBadge   = Color(0xFF10B981)
private val AmberColor   = Color(0xFFF59E0B)
private val BorderCol    = Color(0xFFE5E7EB)

@Composable
fun BerandaMitraScreen(navController: NavHostController) {
    var filterAktif by remember { mutableStateOf("Semua") }
    val filterList = listOf("Semua", "Bersih", "Antar", "Titip")

    val context = LocalContext.current
    var listPesanan by remember { mutableStateOf<List<com.example.tolongin.PesananModel>>(emptyList()) }
    var pesananTersedia by remember { mutableStateOf<List<com.example.tolongin.PesananModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        val sharedPref = context.getSharedPreferences("TolonginPref", android.content.Context.MODE_PRIVATE)
        val emailHelper = sharedPref.getString("USER_EMAIL", "") ?: ""

        com.example.tolongin.RetrofitClient.instance.getPesanan("semua").enqueue(object : retrofit2.Callback<List<com.example.tolongin.PesananModel>> {
            override fun onResponse(call: retrofit2.Call<List<com.example.tolongin.PesananModel>>, response: retrofit2.Response<List<com.example.tolongin.PesananModel>>) {
                if (response.isSuccessful && response.body() != null) {
                    val semua = response.body()!!

                    listPesanan = semua.filter {
                        it.email_helper == emailHelper ||
                                it.status.contains("Progres", ignoreCase = true) ||
                                it.status.contains("progress", ignoreCase = true)
                    }

                    pesananTersedia = semua.filter {
                        (it.email_helper.isNullOrBlank()) &&
                                !it.status.contains("Progres", ignoreCase = true) &&
                                !it.status.contains("progress", ignoreCase = true) &&
                                !it.status.contains("Selesai", ignoreCase = true)
                    }
                }
            }
            override fun onFailure(call: retrofit2.Call<List<com.example.tolongin.PesananModel>>, t: Throwable) {}
        })
    }

    // ── Semua derived state di LUAR LaunchedEffect ──
    val pesananFiltered = when (filterAktif) {
        "Bersih" -> pesananTersedia.filter { it.nama_layanan.contains("Bersih", ignoreCase = true) || it.nama_layanan.contains("Kamar", ignoreCase = true) }
        "Antar"  -> pesananTersedia.filter { it.nama_layanan.contains("Antar", ignoreCase = true) || it.nama_layanan.contains("Kirim", ignoreCase = true) || it.nama_layanan.contains("Paket", ignoreCase = true) }
        "Titip"  -> pesananTersedia.filter { it.nama_layanan.contains("Titip", ignoreCase = true) }
        else     -> pesananTersedia
    }

    val pesananSelesai  = listPesanan.filter { it.status.contains("Selesai", ignoreCase = true) }
    val pesananBerjalan = listPesanan.filter { it.status.contains("Progres", ignoreCase = true) || it.status.contains("progress", ignoreCase = true) }
    val totalPendapatan = pesananSelesai.sumOf { item ->
        item.total_harga.filter { it.isDigit() }.toLongOrNull() ?: 0L
    }
    val pendapatanFormatted = "Rp ${"%,d".format(totalPendapatan).replace(',', '.')}"

    Scaffold(
        containerColor = BgPage,
        bottomBar = { CustomBottomNavMitra(navController = navController, tabAktif = "Beranda") }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {

            // ── TOP BAR ──
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp).statusBarsPadding()
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(28.dp).clip(RoundedCornerShape(6.dp)).background(Primary)) {
                            Text("T", color = Color.White, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.ExtraBold))
                        }
                        Text("Tolong.in", color = Primary, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clip(RoundedCornerShape(9999.dp)).border(1.dp, BorderCol, RoundedCornerShape(9999.dp)).padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(GreenBadge))
                            Text("AKTIF", color = GreenBadge, style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp))
                        }
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(36.dp).clip(CircleShape).background(BgCard)) {
                            Text("🔔", style = TextStyle(fontSize = 16.sp))
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFFEE2E2))
                                .clickable { navController.navigate("login") { popUpTo("beranda_helper") { inclusive = true } } }
                        ) {
                            Text("🚪", style = TextStyle(fontSize = 16.sp))
                        }
                    }
                }
            }

            // ── GREETING ──
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 16.dp)) {
                    Text("HALO MITRA / HELPER", color = TextLight, style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp))
                    Text("Alex Rivera", color = TextDark, style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.ExtraBold))
                }
            }

            // ── PENDAPATAN CARD ──
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 24.dp)
                        .shadow(20.dp, RoundedCornerShape(24.dp), spotColor = Primary.copy(alpha = 0.3f))
                        .clip(RoundedCornerShape(24.dp))
                        .background(Brush.linearGradient(0f to Primary, 0.6f to PrimaryLight, 1f to Color(0xFF5B8FEE), start = Offset(0f, 0f), end = Offset(400f, 300f)))
                        .padding(24.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("PENDAPATAN HARI INI", color = Color.White.copy(alpha = 0.75f), style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.8.sp))
                                Text(pendapatanFormatted, color = Color.White, style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.ExtraBold))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clip(RoundedCornerShape(9999.dp)).background(Color.White.copy(alpha = 0.15f)).padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text("↑", color = GreenBadge, style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold))
                                    Text("+12% vs kemarin", color = Color.White, style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium))
                                }
                            }
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.15f))) {
                                Text("💳", style = TextStyle(fontSize = 20.sp))
                            }
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(PrimaryDark)
                                .clickable { navController.navigate("laporan") }.padding(vertical = 14.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("📊", style = TextStyle(fontSize = 16.sp))
                                Text("Statistik", color = Color.White, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }
            }

            // ── STATS CARDS ──
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 28.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f).shadow(6.dp, RoundedCornerShape(20.dp), spotColor = Color(0xFF3A5A9E).copy(alpha = 0.06f))
                            .clip(RoundedCornerShape(20.dp)).background(Color.White).padding(vertical = 20.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(44.dp).clip(CircleShape).background(Color(0xFFEFF6FF))) {
                            Text("🕐", style = TextStyle(fontSize = 20.sp))
                        }
                        Text("6.5 jam", color = TextDark, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.ExtraBold))
                        Text("Jam Kerja", color = TextLight, style = TextStyle(fontSize = 12.sp))
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f).shadow(6.dp, RoundedCornerShape(20.dp), spotColor = Color(0xFF3A5A9E).copy(alpha = 0.06f))
                            .clip(RoundedCornerShape(20.dp)).background(Color.White).padding(vertical = 20.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(44.dp).clip(CircleShape).background(Color(0xFFFFF7ED))) {
                            Text("✅", style = TextStyle(fontSize = 20.sp))
                        }
                        Text("${pesananSelesai.size}", color = TextDark, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.ExtraBold))
                        Text("Selesai", color = TextLight, style = TextStyle(fontSize = 12.sp))
                    }
                }
            }

            // ── FILTER LAYANAN ──
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                    Text("Filter Layanan", color = TextDark, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold), modifier = Modifier.padding(horizontal = 20.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 20.dp)
                    ) {
                        filterList.forEach { filter ->
                            val aktif = filterAktif == filter
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.clip(RoundedCornerShape(9999.dp))
                                    .background(if (aktif) Primary else Color.White)
                                    .border(1.dp, if (aktif) Primary else BorderCol, RoundedCornerShape(9999.dp))
                                    .clickable { filterAktif = filter }
                                    .padding(horizontal = 18.dp, vertical = 9.dp)
                            ) {
                                Text(filter, color = if (aktif) Color.White else TextMid,
                                    style = TextStyle(fontSize = 13.sp, fontWeight = if (aktif) FontWeight.SemiBold else FontWeight.Normal))
                            }
                        }
                    }
                }
            }

            // ── TUGAS BERJALAN ──
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 20.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text("Tugas Berjalan", color = TextDark, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
                        Text("Lihat Detail", color = Primary, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.clickable { navController.navigate("daftar_pesanan_helper") })
                    }

                    if (pesananBerjalan.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(BgCard).padding(24.dp), contentAlignment = Alignment.Center) {
                            Text("Tidak ada tugas berjalan", color = TextLight, style = TextStyle(fontSize = 14.sp))
                        }
                    } else {
                        pesananBerjalan.take(1).forEach { pesanan ->
                            Box(modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(20.dp), spotColor = Color(0xFF3A5A9E).copy(alpha = 0.07f)).clip(RoundedCornerShape(20.dp)).background(Color.White)) {
                                Box(modifier = Modifier.width(4.dp).fillMaxHeight().align(Alignment.CenterStart).background(Primary))
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)) {
                                    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(Color(0xFFEFF6FF)).padding(horizontal = 8.dp, vertical = 3.dp)) {
                                            Text(
                                                when {
                                                    pesanan.nama_layanan.contains("Bersih", ignoreCase = true) || pesanan.nama_layanan.contains("Kamar", ignoreCase = true) -> "PEMBERSIHAN"
                                                    pesanan.nama_layanan.contains("Antar", ignoreCase = true) || pesanan.nama_layanan.contains("Paket", ignoreCase = true)  -> "ANTAR BARANG"
                                                    pesanan.nama_layanan.contains("Titip", ignoreCase = true) -> "TITIP BELI"
                                                    else -> "LAYANAN"
                                                },
                                                color = Primary, style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                                            )
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Text("🕐", style = TextStyle(fontSize = 11.sp))
                                            Text("Progres", color = AmberColor, style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold))
                                        }
                                    }
                                    Text(pesanan.nama_layanan, color = TextDark, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold))
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.Top) {
                                        Text("📅", style = TextStyle(fontSize = 12.sp))
                                        Text(pesanan.tanggal, color = TextMid, style = TextStyle(fontSize = 12.sp, lineHeight = 1.5.em))
                                    }
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Text("💰", style = TextStyle(fontSize = 12.sp))
                                        Text(pesanan.total_harga, color = Primary, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ── PESANAN TERDEKAT ──
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 16.dp)) {
                    Text("Pesanan Terdekat", color = TextDark, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))

                    if (pesananFiltered.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(BgCard).padding(24.dp), contentAlignment = Alignment.Center) {
                            Text("Tidak ada pesanan untuk filter ini.", color = TextLight, style = TextStyle(fontSize = 13.sp))
                        }
                    } else {
                        pesananFiltered.take(5).forEach { pesanan ->
                            PesananCard(
                                icon = when {
                                    pesanan.nama_layanan.contains("Bersih", ignoreCase = true) || pesanan.nama_layanan.contains("Kamar", ignoreCase = true) -> "🧹"
                                    pesanan.nama_layanan.contains("Antar", ignoreCase = true) || pesanan.nama_layanan.contains("Paket", ignoreCase = true)  -> "🚚"
                                    pesanan.nama_layanan.contains("Titip", ignoreCase = true) -> "🛍️"
                                    else -> "📋"
                                },
                                iconBg = when {
                                    pesanan.nama_layanan.contains("Bersih", ignoreCase = true) || pesanan.nama_layanan.contains("Kamar", ignoreCase = true) -> Color(0xFFEFF6FF)
                                    pesanan.nama_layanan.contains("Antar", ignoreCase = true) || pesanan.nama_layanan.contains("Paket", ignoreCase = true)  -> Color(0xFFEFF6FF)
                                    pesanan.nama_layanan.contains("Titip", ignoreCase = true) -> Color(0xFFFFF7ED)
                                    else -> BgCard
                                },
                                badge = when {
                                    pesanan.nama_layanan.contains("Bersih", ignoreCase = true) || pesanan.nama_layanan.contains("Kamar", ignoreCase = true) -> "PEMBERSIHAN"
                                    pesanan.nama_layanan.contains("Antar", ignoreCase = true) || pesanan.nama_layanan.contains("Paket", ignoreCase = true)  -> "ANTAR BARANG"
                                    pesanan.nama_layanan.contains("Titip", ignoreCase = true) -> "TITIP BELI"
                                    else -> "LAYANAN"
                                },
                                title  = pesanan.nama_layanan,
                                info   = pesanan.tanggal,
                                price  = pesanan.total_harga
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PesananCard(icon: String, iconBg: Color, badge: String, title: String, info: String, price: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().shadow(6.dp, RoundedCornerShape(18.dp), spotColor = Color(0xFF3A5A9E).copy(alpha = 0.06f))
            .clip(RoundedCornerShape(18.dp)).background(Color.White).padding(14.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(iconBg)) {
            Text(icon, style = TextStyle(fontSize = 22.sp))
        }
        Column(verticalArrangement = Arrangement.spacedBy(3.dp), modifier = Modifier.weight(1f)) {
            Text(badge, color = TextLight, style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp))
            Text(title, color = TextDark, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("📅", style = TextStyle(fontSize = 10.sp))
                Text(info, color = TextLight, style = TextStyle(fontSize = 11.sp))
            }
        }
        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(price, color = Primary, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.ExtraBold))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.clip(RoundedCornerShape(9999.dp)).border(1.dp, BorderCol, RoundedCornerShape(9999.dp)).padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text("Ambil", color = TextDark, style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold))
            }
        }
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
fun BerandaMitraPreview() {
    MaterialTheme { BerandaMitraScreen(navController = androidx.navigation.compose.rememberNavController()) }
}