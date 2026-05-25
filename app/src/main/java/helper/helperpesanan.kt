package com.example.tolongin.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

// ── Warna ─────────────────────────────────────────────────────────────────
private val DPrimary    = Color(0xFF1A4FAE)
private val DPrimaryDark= Color(0xFF0D3585)
private val DBgPage     = Color(0xFFFFFFFF)
private val DBgCard     = Color(0xFFF9FAFB)
private val DTextDark   = Color(0xFF111827)
private val DTextMid    = Color(0xFF6B7280)
private val DTextLight  = Color(0xFF9CA3AF)
private val DBorderCol  = Color(0xFFE5E7EB)
private val DBadgeBg    = Color(0xFFF3F4F6)

data class PesananItem(
    val kategori: String,
    val harga: String,
    val judul: String,
    val deskripsi: String,
    val jarak: String,
    val waktu: String
)

@Composable
fun DaftarPesananScreen() {
    var tabAktif by remember { mutableStateOf("Tersedia") }
    val tabList = listOf("Tersedia", "Berjalan", "Selesai")

    val pesananList = listOf(
        PesananItem(
            kategori = "Kebersihan",
            harga = "Rp 50.000",
            judul = "Bersihkan Halaman Rumah",
            deskripsi = "Membutuhkan bantuan untuk menyapu daun kering dan merapikan pot...",
            jarak = "1.2 km",
            waktu = "Hari ini, 14:00"
        ),
        PesananItem(
            kategori = "Kurir",
            harga = "Rp 35.000",
            judul = "Ambil Paket Dokumen",
            deskripsi = "Tolong ambilkan dokumen dari kantor cabang dan antarkan ke rumah sakit...",
            jarak = "3.5 km",
            waktu = "Segera"
        ),
        PesananItem(
            kategori = "Perbaikan",
            harga = "Rp 150.000",
            judul = "Perbaiki Pipa Bocor",
            deskripsi = "Pipa wastafel dapur bocor, butuh tukang ledeng segera untuk...",
            jarak = "0.8 km",
            waktu = "Besok, 09:00"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DBgPage)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp)
        ) {

            // ── TOP BAR ───────────────────────────────────────────────────
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .statusBarsPadding()
                ) {
                    // Logo kiri
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DPrimary)
                    ) {
                        Text("T", color = Color.White,
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.ExtraBold))
                    }

                    // Judul tengah
                    Text(
                        "Tolong.in",
                        color = DPrimary,
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    )

                    // Bell kanan
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DBgCard)
                    ) {
                        Text("🔔", style = TextStyle(fontSize = 16.sp))
                    }
                }
            }

            // ── HEADING ───────────────────────────────────────────────────
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 4.dp, bottom = 20.dp)
                ) {
                    Text(
                        "Pesanan Baru",
                        color = DTextDark,
                        style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                    )
                    Text(
                        "Temukan tugas di sekitarmu",
                        color = DTextMid,
                        style = TextStyle(fontSize = 14.sp)
                    )
                }
            }

            // ── TAB ───────────────────────────────────────────────────────
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 24.dp)
                ) {
                    tabList.forEach { tab ->
                        val aktif = tabAktif == tab
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(9999.dp))
                                .background(if (aktif) DPrimary else Color.Transparent)
                                .clickable { tabAktif = tab }
                                .padding(horizontal = 22.dp, vertical = 10.dp)
                        ) {
                            Text(
                                tab,
                                color = if (aktif) Color.White else DTextMid,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = if (aktif) FontWeight.SemiBold else FontWeight.Normal
                                )
                            )
                        }
                    }
                }
            }

            // ── DAFTAR PESANAN ────────────────────────────────────────────
            items(pesananList.size) { index ->
                val item = pesananList[index]
                PesananCardItem(
                    item = item,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            item { Spacer(Modifier.height(8.dp)) }
        }

        // ── BOTTOM NAV ────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .shadow(
                    16.dp,
                    RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    spotColor = Color(0xFF3A5A9E).copy(alpha = 0.08f)
                )
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color.White)
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                DNavItem(icon = "🏠", label = "Beranda", aktif = false)
                DNavItem(icon = "📋", label = "Pesanan", aktif = true)
                DNavItem(icon = "📊", label = "Laporan", aktif = false)
                DNavItem(icon = "👤", label = "Profil", aktif = false)
            }
        }
    }
}

@Composable
private fun PesananCardItem(
    item: PesananItem,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                8.dp,
                RoundedCornerShape(20.dp),
                spotColor = Color(0xFF3A5A9E).copy(alpha = 0.07f)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        // ── Baris 1: Badge + Harga
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            // Badge kategori
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(DBadgeBg)
                    .border(1.dp, DBorderCol, RoundedCornerShape(9999.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                // icon sesuai kategori
                val badgeIcon = when (item.kategori) {
                    "Kebersihan" -> "🧹"
                    "Kurir"      -> "🚚"
                    "Perbaikan"  -> "🔧"
                    else         -> "📦"
                }
                Text(badgeIcon, style = TextStyle(fontSize = 11.sp))
                Text(
                    item.kategori,
                    color = DTextMid,
                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium)
                )
            }

            // Harga
            Text(
                item.harga,
                color = DPrimary,
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
            )
        }

        // ── Judul
        Text(
            item.judul,
            color = DTextDark,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // ── Deskripsi
        Text(
            item.deskripsi,
            color = DTextMid,
            lineHeight = 1.5.em,
            style = TextStyle(fontSize = 13.sp),
            modifier = Modifier.padding(bottom = 14.dp)
        )

        // ── Divider
        HorizontalDivider(
            color = DBorderCol,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
        )

        // ── Jarak + Waktu
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📍", style = TextStyle(fontSize = 12.sp))
                Text(
                    item.jarak,
                    color = DTextMid,
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🕐", style = TextStyle(fontSize = 12.sp))
                Text(
                    item.waktu,
                    color = DTextMid,
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium)
                )
            }
        }

        // ── Tombol Ambil Pesanan
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(DPrimaryDark)
                .clickable {}
                .padding(vertical = 14.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("✋", style = TextStyle(fontSize = 16.sp))
                Text(
                    "Ambil Pesanan",
                    color = Color.White,
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun DNavItem(icon: String, label: String, aktif: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable {}
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (aktif) DPrimary.copy(alpha = 0.12f) else Color.Transparent)
        ) {
            Text(icon, style = TextStyle(fontSize = 20.sp))
        }
        Text(
            label,
            color = if (aktif) DPrimary else DTextLight,
            style = TextStyle(
                fontSize = 11.sp,
                fontWeight = if (aktif) FontWeight.SemiBold else FontWeight.Normal
            )
        )
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
fun DaftarPesananPreview() {
    DaftarPesananScreen()
}