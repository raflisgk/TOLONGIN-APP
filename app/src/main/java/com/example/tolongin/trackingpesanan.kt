package com.example.tolongin

import android.content.Context // ── IMPORT UNTUK SHAREDPREFERENCES
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext // ── IMPORT UNTUK AMBIL CONTEXT HP
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingPesananScreen(
    navController: NavController,
    namaProduk: String = "Beras Premium 5kg",
    totalBiaya: Int = 70000,
    namaLayanan: String = "Belanja & Antar",
    namaHelper: String = "Andi Saputra"
) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("TolonginPref", Context.MODE_PRIVATE)

    // DETEKSI OTOMATIS: Apakah rute ini sedang membuka Fitur Bersih-bersih?
    val isCleaning = namaProduk.contains("Clean", ignoreCase = true) ||
            namaProduk.contains("Bersih", ignoreCase = true) ||
            namaProduk.contains("Pembersihan", ignoreCase = true)

    // ── DISTRIBUSI DATA DARI CACHE LOKAL SESUAI JENIS FITUR ──
    val itemTampil = if (isCleaning) {
        sharedPref.getString("PREF_CLEAN_LAYANAN", "Cleaning Plus") ?: "Cleaning Plus"
    } else {
        sharedPref.getString("PREF_NAMA_PRODUK", namaProduk) ?: namaProduk
    }

    val alamatTampil = if (isCleaning) {
        sharedPref.getString("PREF_CLEAN_ALAMAT", "Jl. Raya Rungkut, Surabaya") ?: "Jl. Raya Rungkut, Surabaya"
    } else {
        sharedPref.getString("PREF_ALAMAT_TUJUAN", "Jl. Raya Rungkut, Surabaya") ?: "Jl. Raya Rungkut, Surabaya"
    }

    val catatanTampil = if (isCleaning) {
        sharedPref.getString("PREF_CLEAN_CATATAN", "-") ?: "-"
    } else {
        sharedPref.getString("PREF_CATATAN", "-") ?: "-"
    }

    val waktuPengerjaan = sharedPref.getString("PREF_CLEAN_WAKTU", "Hari ini | Segera") ?: "Hari ini | Segera"

    fun formatRupiah(number: Int): String {
        val format = NumberFormat.getNumberInstance(Locale("id", "ID"))
        return "Rp ${format.format(number)}"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Tracking", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color(0xFF1E2D5A))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomNavigationBarTracking(navController = navController)
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    text = "Pantau progres pengerjaan layanan Helper Anda secara real-time.",
                    fontSize = 14.sp,
                    color = Color(0xFF4E5A81),
                    lineHeight = 20.sp
                )
            }

            // --- STATUS BAR BANNER ---
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color(0xFFEFF0FF)).padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(Color(0xFF22C55E)))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isCleaning) "PETUGAS MENUJU LOKASI" else "SEDANG BERLANGSUNG",
                        fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF005AB2)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "15 - 20 Menit",
                        fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF005AB2)
                    )
                }
            }

            // --- KARTU HELPER ---
            item {
                Surface(shape = RoundedCornerShape(16.dp), color = Color.White, shadowElevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFEFF0FF)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF005AB2))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = if(isCleaning) "Siti Rahma (Mitra Clean)" else namaHelper, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Tujuan: $alamatTampil", fontSize = 11.sp, color = Color(0xFF4E5A81), maxLines = 1)
                        }
                        IconButton(onClick = {}, modifier = Modifier.background(Color(0xFF005AB2), CircleShape).size(40.dp)) {
                            Icon(Icons.Default.Chat, contentDescription = "Chat", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            // --- KARTU TRACKING (MAPS DUMMY) ---
            item {
                Column {
                    Text(text = "PROGRES LAYANAN", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4E5A81), letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(shape = RoundedCornerShape(24.dp), color = Color(0xFFF0F5FF).copy(alpha = 0.5f), border = BorderStroke(1.dp, Color(0xFFDBEAFE)), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Box(modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFE2E8F0)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Map, contentDescription = "Peta", tint = Color.Gray, modifier = Modifier.size(48.dp))
                                Box(modifier = Modifier.align(Alignment.Center).offset(y = (-10).dp)) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Red, modifier = Modifier.size(36.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFEFF0FF)), contentAlignment = Alignment.Center) {
                                    // Berubah dinamis: Bersih-bersih pakai emoji sapu (🧹)
                                    if (isCleaning) Text("🧹", fontSize = 20.sp) else Icon(Icons.Default.ShoppingBasket, null, tint = Color(0xFF005AB2))
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(if (isCleaning) "Helper menyiapkan alat kebersihan" else "Helper sedang membelikan item", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
                                    Text("Layanan: $itemTampil", fontSize = 11.sp, color = Color(0xFF4E5A81))
                                }
                            }
                        }
                    }
                }
            }

            // --- RINGKASAN DETAIL ITEM ---
            item {
                Surface(shape = RoundedCornerShape(24.dp), color = Color.White, shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(text = "Ringkasan Layanan", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFFEFF0FF)).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color.White), contentAlignment = Alignment.Center) {
                                Text(if(isCleaning) "🧹" else "📦", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(itemTampil, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A), maxLines = 1)
                                Text(if (isCleaning) "Durasi: Selesai Hari Ini" else "1 Unit", fontSize = 12.sp, color = Color(0xFF4E5A81))
                            }
                            Text(formatRupiah(if(isCleaning) totalBiaya else totalBiaya - 16000), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF005AB2))
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        if (isCleaning) {
                            CostRowTracking("Biaya Jasa Pembersihan", formatRupiah(totalBiaya))
                            CostRowTracking("Jadwal Kunjungan", waktuPengerjaan)
                        } else {
                            CostRowTracking("Harga Produk", formatRupiah(totalBiaya - 16000))
                            CostRowTracking("Layanan Aplikasi", "Rp 1.000")
                            CostRowTracking("Biaya Pengiriman", "Rp 15.000")
                        }
                        CostRowTracking("Catatan Khusus", catatanTampil)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF3F4F6))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Total Pembayaran", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
                            Text(text = formatRupiah(totalBiaya), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF005AB2))
                        }
                    }
                }
            }

            item {
                Text(
                    text = "ID TRANSAKSI: #TLN-CLEAN-99",
                    fontSize = 10.sp, color = Color.Gray, letterSpacing = 2.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun CostRowTracking(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, fontSize = 14.sp, color = Color(0xFF4E5A81), modifier = Modifier.weight(0.4f))
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E2D5A), textAlign = TextAlign.End, modifier = Modifier.weight(0.6f))
    }
}

@Composable
private fun BottomNavigationBarTracking(navController: NavController, modifier: Modifier = Modifier) {
    val navItems = listOf(
        Icons.Outlined.Home to "HOME",
        Icons.Filled.ListAlt to "ORDERS",
        Icons.Outlined.ChatBubbleOutline to "MESSAGES",
        Icons.Outlined.Person to "PROFILE"
    )
    Surface(modifier = modifier.fillMaxWidth(), color = Color.White, shadowElevation = 24.dp, shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            navItems.forEach { (icon, label) ->
                val active = label == "ORDERS"
                val bgColor = if (active) Color(0xFFF0F5FF) else Color.Transparent
                val contentColor = if (active) Color(0xFF2563EB) else Color(0xFF94A3B8)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
                    modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(bgColor).clickable {
                        when (label) {
                            "HOME" -> navController.navigate("beranda") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                            "MESSAGES" -> navController.navigate("pesan") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                            "PROFILE" -> navController.navigate("profil") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                        }
                    }.padding(horizontal = 22.dp, vertical = 12.dp)
                ) {
                    Icon(imageVector = icon, contentDescription = label, tint = contentColor, modifier = Modifier.size(26.dp))
                    Spacer(Modifier.height(4.dp))
                    Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = contentColor)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun TrackingPesananPreview() {
    MaterialTheme {
        TrackingPesananScreen(navController = rememberNavController())
    }
}