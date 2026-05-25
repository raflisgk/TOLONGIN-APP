package com.example.tolongin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavigationBarProfile(navController = navController)
        },
        containerColor = Color(0xFFF8F9FF)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pristine",
                        color = Color(0xFF2563EB),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
                Box(modifier = Modifier.size(120.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.alex_curator), // Ganti sesuai nama file gambar Mas
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(32.dp))
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 8.dp, y = 8.dp)
                            .size(36.dp)
                            .shadow(6.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color(0xFF2563EB))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Profile", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Alex Curator", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
                Text("Anggota Premium Sejak 2023", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))

                Spacer(modifier = Modifier.height(40.dp))
            }

            item {
                SectionTitle("PENGATURAN AKUN")
                Spacer(modifier = Modifier.height(12.dp))

                MenuCard {
                    MenuItem(
                        icon = Icons.Outlined.Person,
                        title = "Informasi Pribadi",
                        subtitle = "Perbarui nama dan rincian kontak",
                        onClick = { navController.navigate("informasipribadi") } // JIKA MAS PUNYA RUTE INI
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle("PREFERENSI")
                Spacer(modifier = Modifier.height(12.dp))

                MenuCard {
                    MenuItem(Icons.Outlined.Notifications, "Notifikasi", "Kontrol peringatan dan pembaruan Anda") {}
                    HorizontalDivider(color = Color(0xFFF3F4F6), modifier = Modifier.padding(horizontal = 16.dp))
                    MenuItem(Icons.Default.HeadsetMic, "Bantuan", "Dapatkan bantuan untuk pesanan Anda") {}
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Button(
                    onClick = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2), contentColor = Color(0xFFDC2626)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Keluar", modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Keluar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

// KOMPONEN PENDUKUNG
@Composable
fun SectionTitle(title: String) {
    Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray, modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp))
}

@Composable
fun MenuCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(shape = RoundedCornerShape(20.dp), color = Color.White, modifier = Modifier.fillMaxWidth(), shadowElevation = 2.dp) {
        Column(content = content)
    }
}

@Composable
fun MenuItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFEEF6FF)),
            contentAlignment = Alignment.Center
        ) { Icon(imageVector = icon, contentDescription = title, tint = Color(0xFF2563EB), modifier = Modifier.size(24.dp)) }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
            Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Detail", tint = Color.LightGray)
    }
}

// --- NAVBAR TERBARU (SAMA DENGAN HOME & ORDERS) ---
@Composable
fun BottomNavigationBarProfile(navController: NavController, modifier: Modifier = Modifier) {
    val navItems = listOf(
        Icons.Outlined.Home to "HOME",
        Icons.Outlined.ListAlt to "ORDERS",
        Icons.Outlined.ChatBubbleOutline to "MESSAGES",
        Icons.Filled.Person to "PROFILE" // Ikon PROFILE menggunakan versi Filled (Tebal)
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
                val active = label == "PROFILE" // Otomatis menyala pada tab PROFILE

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
fun ProfilePreview() {
    MaterialTheme { ProfileScreen(navController = rememberNavController()) }
}