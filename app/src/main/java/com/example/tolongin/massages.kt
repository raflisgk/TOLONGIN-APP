package com.example.tolongin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun PesanScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = { BottomNavigationBarMessages(navController = navController) },
        containerColor = Color(0xFFF8F9FF)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // --- HEADER (Pengganti TopAppBar Kiri-Kanan yang dihapus) ---
            Text(
                text = "Pesan",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E2D5A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Pantau komunikasi dengan Helper Anda",
                fontSize = 14.sp,
                color = Color(0xFF4E5A81)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- TAB SWITCHER (Aktif & Selesai) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEFF0FF))
                    .padding(4.dp)
            ) {
                TabButtonChat(
                    text = "Aktif",
                    isSelected = selectedTab == 0,
                    modifier = Modifier.weight(1f)
                ) { selectedTab = 0 }

                TabButtonChat(
                    text = "Selesai",
                    isSelected = selectedTab == 1,
                    modifier = Modifier.weight(1f)
                ) { selectedTab = 1 }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- DAFTAR CHAT ---
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                if (selectedTab == 0) {
                    item {
                        ChatItem(
                            nama = "Andi Saputra",
                            status = "DALAM TUGAS",
                            layanan = "Perbaikan AC",
                            pesan = "Pak, saya sudah di depan komplek ya, sebentar lagi sampai rumah...",
                            isUnread = true
                        )
                    }
                    item {
                        ChatItem(
                            nama = "Siti Aminah",
                            status = "DIJADWALKAN",
                            layanan = "Pembersihan Rumah",
                            pesan = "Ok, alamat sudah saya catat di sistem ya.",
                            isUnread = false
                        )
                    }
                } else {
                    item {
                        ChatItem(
                            nama = "Budi Santoso",
                            status = "SELESAI",
                            layanan = "Kebun & Taman",
                            pesan = "Terima kasih atas tipsnya, Pak. Senang bisa membantu!",
                            isUnread = false
                        )
                    }
                }

                // --- INFO PROMO BANNER ---
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    PromoBannerCard()
                }
            }
        }
    }
}

// --- KOMPONEN ITEM CHAT ---
@Composable
fun ChatItem(nama: String, status: String, layanan: String, pesan: String, isUnread: Boolean) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Aksi saat chat diklik */ },
        shape = RoundedCornerShape(24.dp),
        color = if (isUnread) Color.White else Color(0xFFF0F5FF).copy(alpha = 0.5f),
        shadowElevation = if (isUnread) 4.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Foto Profil Dummy (Icon)
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(32.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Badge Status
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                when (status) {
                                    "DALAM TUGAS" -> Color(0xFFDBEAFE)
                                    "DIJADWALKAN" -> Color(0xFFFEF3C7)
                                    else -> Color(0xFFF1F5F9)
                                }
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = status,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (status) {
                                "DALAM TUGAS" -> Color(0xFF1D4ED8)
                                "DIJADWALKAN" -> Color(0xFFD97706)
                                else -> Color(0xFF475569)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "• $layanan", fontSize = 10.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = nama, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
                Text(
                    text = pesan,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Unread Dot
            if (isUnread) {
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2563EB))
                )
            }
        }
    }
}

// --- KOMPONEN INFO BANNER ---
@Composable
fun PromoBannerCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(Color(0xFF1D4ED8), Color(0xFF2563EB), Color(0xFF60A5FA))
                )
            )
            .padding(20.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(text = "INFO TOLONG.IN", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Berbagi Lebih Mudah Dengan Wallet",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Top up Tolong-Pay Anda hari ini untuk proses pembayaran otomatis dan aman ke mitra kami.",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

// --- KOMPONEN TAB BUTTON ---
@Composable
fun TabButtonChat(text: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
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

// --- NAVBAR TERBARU (SAMA DENGAN HOME, ORDERS, PROFILE) ---
@Composable
fun BottomNavigationBarMessages(navController: NavController, modifier: Modifier = Modifier) {
    val navItems = listOf(
        Icons.Outlined.Home to "HOME",
        Icons.Outlined.ListAlt to "ORDERS",
        Icons.Filled.ChatBubble to "MESSAGES", // Ikon MESSAGES menggunakan versi Filled
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
                val active = label == "MESSAGES" // Otomatis menyala pada tab MESSAGES

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
                                    navController.navigate("beranda") {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                                "ORDERS" -> {
                                    navController.navigate("daftar_pesanan") {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                                "PROFILE" -> {
                                    navController.navigate("profil") {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                                // MESSAGES tidak butuh navigate karena sedang di sini
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
fun PesanPreview() {
    MaterialTheme { PesanScreen(navController = rememberNavController()) }
}