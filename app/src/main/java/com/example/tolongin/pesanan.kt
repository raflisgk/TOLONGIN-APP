package com.example.tolongin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// --- MODEL DATA ---
data class PesananModel(
    val idTransaksi: String,
    val namaLayanan: String,
    val tanggal: String,
    val status: String,
    val totalHarga: String
)

@Composable
fun PesananScreen(navController: NavController) {
    // State untuk Tab: 0 = Mendatang, 1 = Riwayat
    var selectedTab by remember { mutableStateOf(0) }

    // State untuk memuat data dari database
    var isLoading by remember { mutableStateOf(false) }
    var pesananList by remember { mutableStateOf(listOf<PesananModel>()) }

    // --- LOGIKA MENARIK DATA DARI DATABASE ---
    LaunchedEffect(selectedTab) {
        isLoading = true
        // --- SIMULASI TARIK DATA (GANTI DENGAN RETROFIT NANTI) ---
        if (selectedTab == 0) {
            pesananList = listOf(
                PesananModel("TRX-0012A", "Cleaning Plus", "12 Mei 2026", "Menunggu", "Rp 150.000"),
                PesananModel("TRX-0013B", "Cuci AC", "15 Mei 2026", "Dikonfirmasi", "Rp 75.000")
            )
        } else {
            pesananList = emptyList()
        }
        isLoading = false
    }

    Scaffold(
        bottomBar = { BottomNavigationBarPesanan(navController = navController) },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // --- HEADER ---
            Text(
                text = "Pesanan Saya",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E2D5A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Kelola layanan pilihan Anda",
                fontSize = 16.sp,
                color = Color(0xFF4E5A81)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- CUSTOM TAB SWITCHER ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEFF0FF))
                    .padding(4.dp)
            ) {
                TabButton(
                    text = "Mendatang",
                    isSelected = selectedTab == 0,
                    modifier = Modifier.weight(1f)
                ) { selectedTab = 0 }

                TabButton(
                    text = "Riwayat",
                    isSelected = selectedTab == 1,
                    modifier = Modifier.weight(1f)
                ) { selectedTab = 1 }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- KONTEN DAFTAR PESANAN ---
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF2563EB))
                }
            } else if (pesananList.isEmpty()) {
                // TAMPILAN JIKA BELUM ADA PESANAN
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Inbox,
                            contentDescription = "Kosong",
                            tint = Color.LightGray,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Belum ada pesanan di sini",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4E5A81)
                        )
                        Text(
                            text = "Yuk, buat pesanan pertamamu sekarang!",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                // TAMPILAN JIKA ADA PESANAN
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(pesananList) { pesanan ->
                        CardPesanan(pesanan)
                    }
                }
            }
        }
    }
}

// --- KOMPONEN PENDUKUNG ---

@Composable
fun TabButton(text: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color.White else Color.Transparent)
            .clickable { onClick() }
            .then(if (isSelected) Modifier.shadow(2.dp, RoundedCornerShape(12.dp)) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color(0xFF2563EB) else Color(0xFF64748B),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun CardPesanan(pesanan: PesananModel) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pesanan.idTransaksi,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEEF6FF))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = pesanan.status, color = Color(0xFF2563EB), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = pesanan.namaLayanan, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = pesanan.tanggal, fontSize = 14.sp, color = Color.Gray)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF3F4F6))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Total Pembayaran", fontSize = 12.sp, color = Color.Gray)
                Text(text = pesanan.totalHarga, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
            }
        }
    }
}

// --- NAVBAR TERBARU (SAMA DENGAN HOME) ---
@Composable
fun BottomNavigationBarPesanan(navController: NavController, modifier: Modifier = Modifier) {
    val navItems = listOf(
        Icons.Outlined.Home to "HOME",
        Icons.Filled.ListAlt to "ORDERS", // Ikon ORDERS menggunakan versi Filled (Tebal)
        Icons.Outlined.ChatBubbleOutline to "MESSAGES",
        Icons.Outlined.Person to "PROFILE"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 24.dp,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { (icon, label) ->
                val active = label == "ORDERS"

                val bgColor = if (active) Color(0xFFF0F5FF) else Color.Transparent
                val contentColor = if (active) Color(0xFF2563EB) else Color(0xFF94A3B8)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(bgColor)
                        .clickable {
                            when (label) {
                                "HOME" -> {
                                    if (active) return@clickable // Biar nggak reload kalau ditekan di halaman yang sama
                                    navController.navigate("beranda") {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                                "ORDERS" -> {
                                    if (active) return@clickable
                                    navController.navigate("daftar_pesanan") {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                                "MESSAGES" -> {
                                    if (active) return@clickable
                                    // INI JALUR BARU MENUJU LAYAR PESAN
                                    navController.navigate("pesan") {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                                "PROFILE" -> {
                                    if (active) return@clickable
                                    navController.navigate("profil") {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        }
                        .padding(horizontal = 22.dp, vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = contentColor,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                        color = contentColor
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun PesananPreview() {
    MaterialTheme {
        PesananScreen(navController = rememberNavController())
    }
}