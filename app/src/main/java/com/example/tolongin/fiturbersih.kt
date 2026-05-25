package com.example.tolongin // Sesuaikan dengan nama package project Mas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// Warna Tema
private val BiruTua = Color(0xFF005AB2)
private val BiruMuda = Color(0xFF64A1FF)
private val TeksGelap = Color(0xFF212D51)
private val TeksAbu = Color(0xFF4E5A81)
private val LatarBiruMuda = Color(0xFFE3E7FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PembersihanScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Detail Layanan", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TeksGelap) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = BiruTua)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 1. JUDUL UTAMA
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = TeksGelap, fontSize = 28.sp, fontWeight = FontWeight.Light)) { append("Pilih Layanan\n") }
                    withStyle(style = SpanStyle(color = BiruTua, fontSize = 28.sp, fontWeight = FontWeight.Bold)) { append("Kebersihan Terbaik") }
                },
                lineHeight = 34.sp
            )

            Text(
                text = "Dari hunian mewah hingga kamar kos yang nyaman, kami memastikan setiap sudut bersinar dengan sentuhan profesional.",
                color = TeksAbu,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )

            // 2. KARTU PAKET 1: PEMBERSIHAN RUMAH
            PaketLayananCard(
                title = "Pembersihan Rumah",
                price = "Rp 250rb",
                priceLabel = "MULAI DARI",
                description = "Layanan menyeluruh untuk rumah, apartemen, atau villa. Termasuk pembersihan mendalam (deep cleaning) dan sanitasi total.",
                badgeText = "PREMIUM SERVICE",
                features = listOf(
                    FeatureData(Icons.Default.Schedule, "4-6 Jam Kerja"),
                    FeatureData(Icons.Default.VerifiedUser, "Alat Lengkap"),
                    FeatureData(Icons.Default.Shield, "Bergaransi")
                ),
                buttonText = "Pesan Layanan Rumah",
                onOrderClick = {
                    navController.navigate(
                        "pemesanan/Pembersihan Rumah/Rp 250.000"
                    )
                }
            )

            // 3. PROMO CARD
            Surface(
                color = Color(0xFFF9FAFB),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFF3F4F6)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(color = Color(0xFF853D97), shape = RoundedCornerShape(99.dp)) {
                        Text("PROMO MINGGUAN", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                    }
                    Text("Langganan Hemat", color = Color(0xFF590E6D), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Dapatkan diskon 20% untuk pembersihan rutin 4x sebulan.", color = TeksAbu, fontSize = 13.sp)
                }
            }

            // 4. KARTU PAKET 2: KAMAR KOS (YANG SEMPAT HILANG)
            PaketLayananCard(
                title = "Kamar Kos",
                price = "Rp 75rb",
                priceLabel = "FLAT RATE",
                description = "Solusi cepat dan hemat untuk mahasiswa dan pekerja. Fokus pada area tidur, meja kerja, dan kamar mandi dalam.",
                badgeText = "BEST VALUE",
                features = listOf(
                    FeatureData(Icons.Default.VerifiedUser, "Pembersih Terverifikasi"),
                    FeatureData(Icons.Default.Eco, "Cairan Ramah Lingkungan")
                ),
                buttonText = "Pilih Paket Kos",
                onOrderClick = {
                    navController.navigate(
                        "pemesanan/Kamar Kos/Rp 75.000"
                    )
                }
            )

            // 5. KENAPA MEMILIH KAMI?
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Standar Kebersihan\nTanpa Kompromi", color = TeksGelap, fontSize = 22.sp, fontWeight = FontWeight.Bold, lineHeight = 28.sp)

                InfoItem("Pembersih Terverifikasi", "Setiap mitra kami melewati seleksi ketat dan pelatihan intensif.")
                InfoItem("Cairan Ramah Lingkungan", "Aman untuk keluarga, hewan peliharaan, dan lingkungan sekitar.")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Data class khusus untuk fitur agar ikon bisa dinamis
data class FeatureData(val icon: ImageVector, val text: String)

// CETAKAN KARTU LAYANAN (Bisa dipakai berulang kali)
@Composable
fun PaketLayananCard(
    title: String,
    price: String,
    priceLabel: String,
    description: String,
    badgeText: String,
    features: List<FeatureData>,
    buttonText: String,
    onOrderClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White)
            .border(BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f)), RoundedCornerShape(32.dp))
    ) {
        Column {
            // Placeholder Gambar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFFE5E7EB)),
                contentAlignment = Alignment.Center
            ) {
                Text("Gambar: $title", color = Color.Gray, fontWeight = FontWeight.Bold)

                // Chip Badge di pojok kiri atas
                Surface(
                    shape = RoundedCornerShape(99.dp),
                    color = BiruTua,
                    modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
                ) {
                    Text(badgeText, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), letterSpacing = 1.sp)
                }
            }

            // Area Teks & Info Harga
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(title, color = TeksGelap, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Column(horizontalAlignment = Alignment.End) {
                        Text(priceLabel, color = TeksAbu, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(price, color = BiruTua, fontSize = 20.sp, fontWeight = FontWeight.Black)
                    }
                }

                Text(description, color = TeksAbu, fontSize = 13.sp, lineHeight = 18.sp)

                // Render List Fitur dengan sistem wrap otomatis
                @OptIn(ExperimentalLayoutApi::class)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    features.forEach { feature ->
                        FeatureBadge(feature.icon, feature.text)
                    }
                }

                // Tombol Pesan Khusus Kartu Ini
                Button(
                    onClick = onOrderClick,
                    modifier = Modifier.fillMaxWidth().height(52.dp).padding(top = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.linearGradient(listOf(BiruTua, BiruMuda))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(buttonText, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureBadge(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(LatarBiruMuda)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = BiruTua, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, color = TeksAbu, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun InfoItem(title: String, desc: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(99.dp)).background(BiruMuda.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BiruTua, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, color = TeksGelap, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(desc, color = TeksAbu, fontSize = 13.sp, lineHeight = 18.sp)
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
fun PreviewPembersihan() {
    PembersihanScreen(navController = rememberNavController())
}