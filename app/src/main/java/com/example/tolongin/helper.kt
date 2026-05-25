package com.example.app // Ganti dengan package-mu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Warna-warna Utama Sesuai Desain
val PrimaryBlue = Color(0xFF0056D2)
val LightBlueBg = Color(0xFFE8F0FE)
val BackgroundColor = Color(0xFFF8F9FA)
val TextDarkBlue = Color(0xFF1A237E)
val TextGray = Color(0xFF757575)

@Composable
fun PopularHelpersApp() {
    Scaffold(
        containerColor = BackgroundColor,
        topBar = { TopNavigationBar() },
        bottomBar = { CustomBottomNavBar() } // Menggunakan Desain Gambar 2
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Section: Pilihan Terbaik
            item { TopPickSection() }

            // Section: Scroll Horizontal (Budi Santoso & Lusi Rahma)
            item { HorizontalHelperCards() }

            // Section: Helper Lainnya
            item { OtherHelpersSection() }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun TopNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = PrimaryBlue
        )
        Text(
            text = "Popular Helpers",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = TextDarkBlue
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = TextGray)
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextGray)
        }
    }
}

@Composable
fun TopPickSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pilihan Terbaik",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextDarkBlue
            )
            Text(
                text = "LIHAT SEMUA",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Kartu Utama Siti Aminah
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.LightGray) // GANTI INI DENGAN IMAGE SITI AMINAH
        ) {
            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 100f
                        )
                    )
            )

            // Konten dalam Kartu Utama
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryBlue)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("TOP RATED", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.Star, contentDescription = "Star", tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("4.9", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text("Siti Aminah", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Spesialis Pembersihan", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Pesan Jasa", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun HorizontalHelperCards() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card 1
        SmallHelperCard(
            modifier = Modifier.weight(1f),
            name = "Budi Santoso",
            role = "Kurir Barang Ringan",
            rating = "4.8"
        )
        // Card 2
        SmallHelperCard(
            modifier = Modifier.weight(1f),
            name = "Lusi Rahma",
            role = "Asisten Belanja",
            rating = "4.7"
        )
    }
}

@Composable
fun SmallHelperCard(modifier: Modifier = Modifier, name: String, role: String, rating: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture with Verified Badge
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray) // GANTI DENGAN IMAGE
                )
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Verified",
                    tint = PrimaryBlue,
                    modifier = Modifier
                        .size(20.dp)
                        .offset(x = 8.dp, y = 8.dp)
                        .background(Color.White, CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(name, fontWeight = FontWeight.Bold, color = TextDarkBlue, fontSize = 14.sp)
            Text(role, color = TextGray, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = "Star", tint = Color(0xFFFFC107), modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(rating, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextDarkBlue)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = LightBlueBg),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Pesan Jasa", color = PrimaryBlue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun OtherHelpersSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Helper Lainnya",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextDarkBlue
        )

        ListHelperItem(name = "Andi Wijaya", role = "Perbaikan Rumah", rating = "4.9", status = "TERSEDIA", statusColor = PrimaryBlue)
        ListHelperItem(name = "Dewi Lestari", role = "Pendamping Anak", rating = "4.8", status = "TERSEDIA", statusColor = PrimaryBlue)
        ListHelperItem(name = "Rişky Pratama", role = "Servis Elektronik", rating = "4.6", status = "SIBUK", statusColor = Color(0xFF9C27B0))
    }
}

@Composable
fun ListHelperItem(name: String, role: String, rating: String, status: String, statusColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FA)) // Sedikit abu-abu kebiruan
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray) // GANTI DENGAN IMAGE
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold, color = TextDarkBlue, fontSize = 16.sp)
                Text(role, color = TextGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(status, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Star", tint = Color(0xFFFFC107), modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(rating, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextDarkBlue)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Pesan Jasa", color = PrimaryBlue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Go", tint = PrimaryBlue, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

// ==== BOTTOM NAVIGATION SESUAI GAMBAR 2 ====
@Composable
fun CustomBottomNavBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Item 1: HOME (Aktif dengan background Light Blue)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(LightBlueBg) // Latar belakang biru muda spesifik desain 2
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Home, contentDescription = "Home", tint = PrimaryBlue, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("HOME", color = PrimaryBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Item 2: ORDERS (Menggunakan ikon Menu sebagai pengganti List/Nota)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Menu, contentDescription = "Orders", tint = Color(0xFF90A4AE), modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text("ORDERS", color = Color(0xFF90A4AE), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
            }

// Item 3: MESSAGES (Menggunakan ikon Email sebagai pengganti Chat)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Email, contentDescription = "Messages", tint = Color(0xFF90A4AE), modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text("MESSAGES", color = Color(0xFF90A4AE), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
            }

            // Item 4: PROFILE (Non-aktif)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Outlined.Person, contentDescription = "Profile", tint = Color(0xFF90A4AE), modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text("PROFILE", color = Color(0xFF90A4AE), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        PopularHelpersApp()
    }
}