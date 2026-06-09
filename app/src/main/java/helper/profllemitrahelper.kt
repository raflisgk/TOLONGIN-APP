package com.example.tolongin.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

// ── WARNA TEMA DESAIN ──
private val DPrimary = Color(0xFF2563EB) // Biru utama
private val DBg = Color(0xFFF7F8FA)
private val DTextDark = Color(0xFF111827)
private val DTextMid = Color(0xFF6B7280)

// ── MODEL DATA PROFIL ──
data class ProfilMitraModel(
    val nama: String = "Budi Santoso",
    val noHp: String = "+62 812-3456-7890",
    val rating: String = "4.9",
    val totalUlasan: String = "(124 Ulasan)",
    val pekerjaan: String = "245",
    val tingkat: String = "Senior",
    val bergabung: String = "2022",
    var isCleaning: Boolean = true,
    var isAntar: Boolean = true,
    var isTitip: Boolean = false,
    var jadwalHari: String = "Senin - Sabtu",
    var jadwalJam: String = "08:00 - 17:00"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilMitraScreen(navController: NavHostController) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val coroutineScope = rememberCoroutineScope()

    // ── 1. INISIALISASI SHARED PREFERENCES ──
    val sharedPref = context.getSharedPreferences("MitraPrefs", android.content.Context.MODE_PRIVATE)

    // ── 2. BACA STATUS TERAKHIR DARI MEMORI HP ──
    var profil by remember {
        mutableStateOf(
            ProfilMitraModel(
                isCleaning = sharedPref.getBoolean("isCleaning", true),
                isAntar = sharedPref.getBoolean("isAntar", true),
                isTitip = sharedPref.getBoolean("isTitip", false)
            )
        )
    }

    var isLoading by remember { mutableStateOf(!isPreview) }

    // State Bottom Sheet
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // ── AMBIL DATA DARI DATABASE SAAT HALAMAN DIBUKA ──
    LaunchedEffect(Unit) {
        if (!isPreview) {
            kotlinx.coroutines.delay(800) // Simulasi loading

            // Perbaikan: Pakai .copy() agar setelan layanan tidak kereset!
            profil = profil.copy(nama = "Budi Santoso")
            isLoading = false
        }
    }

    Scaffold(
        containerColor = DBg,
        topBar = {
            // Top Bar Kustom
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 20.dp, vertical = 16.dp).statusBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(28.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFF1E3A8A)), contentAlignment = Alignment.Center) {
                        Text("T", color = Color.White, fontWeight = FontWeight.ExtraBold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tolong.in", color = Color(0xFF1E3A8A), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                }
                Icon(Icons.Default.Notifications, contentDescription = "Notif", tint = DTextMid)
            }
        },
        bottomBar = {
            CustomBottomNavMitra(navController = navController, tabAktif = "Profil")
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = DPrimary) }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                // ── 1. KARTU PROFIL UTAMA ──
                item {
                    Card(
                        shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                            // Foto Profil & Badge
                            Box {
                                Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFFE5E7EB)), contentAlignment = Alignment.Center) {
                                    Text("👨‍🔧", fontSize = 40.sp)
                                }
                                Box(
                                    modifier = Modifier.align(Alignment.BottomEnd).offset(x = 4.dp, y = 4.dp).size(24.dp).clip(CircleShape)
                                        .background(DPrimary).border(2.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) { Icon(Icons.Default.Check, contentDescription = "Verified", tint = Color.White, modifier = Modifier.size(14.dp)) }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(profil.nama, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DTextDark)
                            Text(profil.noHp, fontSize = 14.sp, color = DTextMid)
                            Spacer(modifier = Modifier.height(8.dp))
                            // Badge Rating
                            Row(
                                modifier = Modifier.clip(RoundedCornerShape(99.dp)).background(Color(0xFFF3F4F6)).padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("⭐", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(profil.rating, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(profil.totalUlasan, fontSize = 12.sp, color = DTextMid)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            HorizontalDivider(color = Color(0xFFF3F4F6))
                            Spacer(modifier = Modifier.height(16.dp))
                            // Statistik Bawah
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                StatItem("PEKERJAAN", profil.pekerjaan, DPrimary)
                                VerticalDivider(modifier = Modifier.height(30.dp), color = Color(0xFFE5E7EB))
                                StatItem("TINGKAT", profil.tingkat, DPrimary)
                                VerticalDivider(modifier = Modifier.height(30.dp), color = Color(0xFFE5E7EB))
                                StatItem("BERGABUNG", profil.bergabung, DTextDark)
                            }
                        }
                    }
                }

                // ── 2. LAYANAN AKTIF ──
                item {
                    Card(
                        shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
                                Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFEFF6FF)), contentAlignment = Alignment.Center) {
                                    Text("⚙️", fontSize = 18.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Layanan Aktif", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }

                            LayananRow(ikon = "🧹", nama = "Cleaning", isAktif = profil.isCleaning) { newVal ->
                                profil = profil.copy(isCleaning = newVal)
                                sharedPref.edit().putBoolean("isCleaning", newVal).apply()
                                updateLayananDatabase(context, "Cleaning", newVal)
                            }
                            LayananRow(ikon = "🚚", nama = "Antar", isAktif = profil.isAntar) { newVal ->
                                profil = profil.copy(isAntar = newVal)
                                sharedPref.edit().putBoolean("isAntar", newVal).apply()
                                updateLayananDatabase(context, "Antar", newVal)
                            }
                            LayananRow(ikon = "🛍️", nama = "Titip", isAktif = profil.isTitip) { newVal ->
                                profil = profil.copy(isTitip = newVal)
                                sharedPref.edit().putBoolean("isTitip", newVal).apply()
                                updateLayananDatabase(context, "Titip", newVal)
                            }
                        }
                    }
                }

                // ── 3. AREA KERJA ──
                item {
                    Card(
                        shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), modifier = Modifier.fillMaxWidth()
                    ) {
                        Box {
                            Box(modifier = Modifier.fillMaxWidth().height(160.dp).background(Color(0xFFF9FAFB))) {
                                Text("🗺️ Maps Background", color = Color.LightGray, modifier = Modifier.align(Alignment.Center).padding(bottom = 40.dp))
                            }
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFDBEAFE)), contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.LocationOn, contentDescription = "Lokasi", tint = DPrimary, modifier = Modifier.size(20.dp))
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text("Area Kerja", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Detail", tint = DTextMid)
                                }
                                Spacer(modifier = Modifier.height(40.dp))
                                Text("WILAYAH UTAMA", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DTextMid, letterSpacing = 0.5.sp)
                                Text("Jakarta Selatan", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp, bottom = 8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Pill("Kebayoran"); Pill("Kemang"); Pill("+2")
                                }
                            }
                        }
                    }
                }

                // ── 4. JADWAL KERJA ──
                item {
                    Card(
                        shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth().clickable { showSheet = true }
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFFFF7ED)), contentAlignment = Alignment.Center) {
                                        Text("🕒", fontSize = 18.sp)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Jadwal Kerja", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = DTextMid, modifier = Modifier.size(20.dp))
                            }
                            JadwalRow("HARI", "6", profil.jadwalHari, "Minggu Libur")
                            Spacer(modifier = Modifier.height(12.dp))
                            JadwalRow("JAM", "⏱️", profil.jadwalJam, "Waktu Indonesia Barat")
                        }
                    }
                }

                // ── 5. TOMBOL KELUAR AKUN ──
                item {
                    Button(
                        onClick = {
                            Toast.makeText(context, "Keluar Akun Berhasil", Toast.LENGTH_SHORT).show()
                            navController.navigate("login") { popUpTo(0) { inclusive = true } }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2)),
                        shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().height(55.dp)
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Keluar", tint = Color(0xFFDC2626))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Keluar Akun", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                item { Spacer(modifier = Modifier.height(30.dp)) }
            }
        }

        // ── BOTTOM SHEET UBAH JADWAL ──
        if (showSheet) {
            ModalBottomSheet(onDismissRequest = { showSheet = false }, sheetState = sheetState, containerColor = Color.White) {
                var inputHari by remember { mutableStateOf(profil.jadwalHari) }
                var inputJam by remember { mutableStateOf(profil.jadwalJam) }

                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 40.dp)) {
                    Text("Ubah Jadwal Kerja", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 20.dp))
                    OutlinedTextField(value = inputHari, onValueChange = { inputHari = it }, label = { Text("Hari Kerja") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = inputJam, onValueChange = { inputJam = it }, label = { Text("Jam Operasional") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            profil = profil.copy(jadwalHari = inputHari, jadwalJam = inputJam)
                            updateJadwalDatabase(context, inputHari, inputJam)
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion { if (!sheetState.isVisible) showSheet = false }
                        },
                        modifier = Modifier.fillMaxWidth().height(55.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = DPrimary)
                    ) { Text("Simpan Jadwal", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

// ── FUNGSI API RETROFIT (DUMMY) ──
fun updateLayananDatabase(context: android.content.Context, namaLayanan: String, status: Boolean) {
    val statText = if (status) "Diaktifkan" else "Dimatikan"
    Toast.makeText(context, "Layanan $namaLayanan $statText", Toast.LENGTH_SHORT).show()
}

fun updateJadwalDatabase(context: android.content.Context, hari: String, jam: String) {
    Toast.makeText(context, "Jadwal berhasil diupdate", Toast.LENGTH_SHORT).show()
}

// ── KOMPONEN PENDUKUNG UI ──
@Composable
fun StatItem(title: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DTextMid, letterSpacing = 0.5.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

@Composable
fun LayananRow(ikon: String, nama: String, isAktif: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp)).padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(ikon, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text(nama, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = DTextDark)
        }
        Switch(checked = isAktif, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedTrackColor = DPrimary))
    }
}

@Composable
fun JadwalRow(titleLabel: String, titleIcon: String, mainText: String, subText: String) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color(0xFFF9FAFB), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp)).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.size(48.dp).background(Color(0xFFE5E7EB), RoundedCornerShape(8.dp)),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
        ) {
            Text(titleLabel, fontSize = 10.sp, color = DTextMid, fontWeight = FontWeight.Bold)
            Text(titleIcon, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DPrimary)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(mainText, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DTextDark)
            Text(subText, fontSize = 12.sp, color = DTextMid)
        }
    }
}

@Composable
fun Pill(text: String) {
    Box(modifier = Modifier.background(Color(0xFFE5E7EB), RoundedCornerShape(99.dp)).padding(horizontal = 12.dp, vertical = 4.dp)) {
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = DTextDark)
    }
}

// ── NAVBAR KUSTOM PUSAT (BERSIH) ──
@Composable
fun CustomBottomNavMitra(navController: NavHostController, tabAktif: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(24.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp), spotColor = Color.Black.copy(alpha = 0.1f))
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(Color.White)
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {

            DNavItem(icon = "🏠", label = "Beranda", aktif = tabAktif == "Beranda") {
                if (tabAktif != "Beranda") navController.navigate("beranda_helper") { popUpTo(0) }
            }

            DNavItem(icon = "📋", label = "Pesanan", aktif = tabAktif == "Pesanan") {
                if (tabAktif != "Pesanan") navController.navigate("daftar_pesanan_helper") { popUpTo(0) }
            }

            DNavItem(icon = "📊", label = "Laporan", aktif = tabAktif == "Laporan") {
                if (tabAktif != "Laporan") {
                    try {
                        navController.navigate("laporan") {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    } catch (e: Exception) {
                        Toast.makeText(navController.context, "Rute laporan belum didaftarkan", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            DNavItem(icon = "👤", label = "Profil", aktif = tabAktif == "Profil") {
                if (tabAktif != "Profil") {
                    try {
                        navController.navigate("profil_mitra") {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    } catch (e: Exception) {
                        Toast.makeText(navController.context, "Rute profil_mitra belum didaftarkan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

// ── DESAIN TOMBOL NAVBAR ──
@Composable
fun DNavItem(icon: String, label: String, aktif: Boolean, onClick: () -> Unit) {
    val DPrimary = Color(0xFF2563EB)
    val DTextMid = Color(0xFF6B7280)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .background(if (aktif) Color(0xFFDBEAFE) else Color.Transparent, RoundedCornerShape(14.dp))
        ) {
            Text(icon, fontSize = 22.sp)
        }
        Text(
            text = label,
            color = if (aktif) DPrimary else DTextMid,
            fontSize = 11.sp,
            fontWeight = if (aktif) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
fun ProfilPreview() {
    MaterialTheme { ProfilMitraScreen(rememberNavController()) }
}