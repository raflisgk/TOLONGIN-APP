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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

// ── Warna ─────────────────────────────────────────────────────────────────
private val PBPrimary    = Color(0xFF1A4FAE)
private val PBPrimaryDk  = Color(0xFF0D3585)
private val PBBgPage     = Color(0xFFFFFFFF)
private val PBBgCard     = Color(0xFFF9FAFB)
private val PBTextDark   = Color(0xFF111827)
private val PBTextMid    = Color(0xFF6B7280)
private val PBTextLight  = Color(0xFF9CA3AF)
private val PBBorderCol  = Color(0xFFE5E7EB)
private val PBOrangeText = Color(0xFFEA580C)
private val PBOrangeBg   = Color(0xFFFFF7ED)

@Composable
fun PesananBerjalanScreen(
    onBack: () -> Unit = {},
    onSelesai: () -> Unit = {}
) {
    // Step progress: 0=Terima,1=Menuju,2=Sampai,3=Kerja,4=Hampir,5=Selesai
    val stepAktif = 3
    val stepList = listOf("Terima", "Menuju", "Sampai", "Kerja", "Hampir", "Selesai")

    val riwayatList = listOf(
        Triple("Sedang Dikerjakan", "10:45 WIB - Teknisi sedang melakukan pengecekan freon dan filter AC.", true),
        Triple("Sampai di Lokasi", "10:30 WIB", false),
        Triple("Menuju Lokasi Pelanggan", "09:50 WIB", false)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PBBgPage)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 88.dp)
        ) {

            // ── TOP BAR ───────────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .statusBarsPadding()
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable { onBack() }
                    ) {
                        Text("←", color = PBTextDark,
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                    }
                    Text(
                        "Detail Pesanan",
                        color = PBTextDark,
                        style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // ── STATUS + EST. SELESAI ─────────────────────────────────────
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp, bottom = 12.dp)
                ) {
                    Text(
                        "Sedang Dikerjakan",
                        color = PBPrimary,
                        style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                    )
                    Text(
                        "Est. Selesai: 14:00",
                        color = PBTextMid,
                        style = TextStyle(fontSize = 13.sp)
                    )
                }
            }

            // ── STEP PROGRESS ─────────────────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp)
                ) {
                    // Dots + lines row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        stepList.forEachIndexed { index, _ ->
                            val selesai = index <= stepAktif
                            val aktif   = index == stepAktif

                            // Dot
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(if (aktif) 18.dp else 14.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            aktif  -> PBPrimary
                                            selesai -> PBPrimary
                                            else   -> Color(0xFFD1D5DB)
                                        }
                                    )
                            ) {
                                if (aktif) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                    )
                                }
                            }

                            // Garis antara dot
                            if (index < stepList.lastIndex) {
                                val lineSelesai = index < stepAktif
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(3.dp)
                                        .background(
                                            if (lineSelesai) PBPrimary else Color(0xFFE5E7EB)
                                        )
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(6.dp))

                    // Label step
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        stepList.forEachIndexed { index, label ->
                            val aktif = index == stepAktif
                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text(
                                    label,
                                    color = if (aktif) PBPrimary else PBTextLight,
                                    style = TextStyle(
                                        fontSize = 9.sp,
                                        fontWeight = if (aktif) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // ── INFO PESANAN CARD ─────────────────────────────────────────
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 16.dp)
                        .shadow(6.dp, RoundedCornerShape(16.dp),
                            spotColor = Color(0xFF3A5A9E).copy(alpha = 0.07f))
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                ) {
                    // Baris 1: Judul + badge Darurat
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp)
                    ) {
                        Column {
                            Text(
                                "Perbaikan AC Split Biking",
                                color = PBTextDark,
                                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                            )
                            Text(
                                "ID Pesanan: #TLG-9824",
                                color = PBTextLight,
                                style = TextStyle(fontSize = 11.sp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(PBOrangeBg)
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                "Darurat",
                                color = PBOrangeText,
                                style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }

                    HorizontalDivider(color = PBBorderCol,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp))

                    // Baris 2: Avatar pelanggan + aksi chat/telepon
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 14.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF8FA8C8))
                            ) {
                                Text("AW", color = Color.White,
                                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold))
                            }
                            Column {
                                Text("Bapak Anton Wijaya", color = PBTextDark,
                                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
                                Text("Pelanggan Baru", color = PBTextMid,
                                    style = TextStyle(fontSize = 12.sp))
                            }
                        }
                        // Tombol chat + telepon
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEFF6FF))
                                    .clickable {}
                            ) {
                                Text("💬", style = TextStyle(fontSize = 16.sp))
                            }
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEFF6FF))
                                    .clickable {}
                            ) {
                                Text("📞", style = TextStyle(fontSize = 16.sp))
                            }
                        }
                    }

                    // Baris 3: Alamat
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 12.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF0F4FF))
                        ) {
                            Text("📍", style = TextStyle(fontSize = 13.sp))
                        }
                        Column {
                            Text(
                                "Perumahan Indah Asri Blok C4 No. 12",
                                color = PBTextDark,
                                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                "Kec. Kebayoran Baru, Jakarta Selatan",
                                color = PBTextMid,
                                style = TextStyle(fontSize = 12.sp)
                            )
                        }
                    }

                    HorizontalDivider(color = PBBorderCol,
                        modifier = Modifier.padding(horizontal = 16.dp))

                    // Baris 4: Estimasi Biaya
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("💳", style = TextStyle(fontSize = 14.sp))
                            Text("Estimasi Biaya", color = PBTextMid,
                                style = TextStyle(fontSize = 13.sp))
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Rp 150.000", color = PBPrimary,
                                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.ExtraBold))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFF3F4F6))
                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Text("Tunai", color = PBTextMid,
                                    style = TextStyle(fontSize = 11.sp))
                            }
                        }
                    }
                }
            }

            // ── LOKASI PENGERJAAN ─────────────────────────────────────────
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp)
                        .shadow(6.dp, RoundedCornerShape(16.dp),
                            spotColor = Color(0xFF3A5A9E).copy(alpha = 0.06f))
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Text("Lokasi Pengerjaan", color = PBTextDark,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.ExtraBold))

                    // Peta placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    0f to Color(0xFFB8D4E8),
                                    0.5f to Color(0xFFCDE3F0),
                                    1f to Color(0xFFB0CCDF),
                                    start = Offset(0f, 0f),
                                    end = Offset(400f, 200f)
                                )
                            )
                    ) {
                        // Simulasi jalan
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .align(Alignment.Center)
                                .background(Color.White.copy(alpha = 0.5f))
                        )
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .fillMaxHeight()
                                .align(Alignment.Center)
                                .background(Color.White.copy(alpha = 0.5f))
                        )
                        // Pin merah
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFDC2626))
                            ) {
                                Text("📍", style = TextStyle(fontSize = 14.sp))
                            }
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(8.dp)
                                    .background(Color(0xFFDC2626))
                            )
                        }
                    }

                    // Tombol Buka di Maps
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, PBBorderCol, RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .clickable {}
                            .padding(vertical = 12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🗺", style = TextStyle(fontSize = 15.sp))
                            Text("Buka di Maps", color = PBPrimary,
                                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold))
                        }
                    }
                }
            }

            // ── RIWAYAT UPDATE ────────────────────────────────────────────
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Text("Riwayat Update", color = PBTextDark,
                        style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.padding(bottom = 16.dp))

                    riwayatList.forEachIndexed { index, (status, waktuDeskripsi, aktif) ->
                        val isLast = index == riwayatList.lastIndex
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Timeline
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(18.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(if (aktif) PBPrimary else Color(0xFFD1D5DB))
                                )
                                if (!isLast) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(60.dp)
                                            .background(PBBorderCol)
                                    )
                                }
                            }
                            // Konten
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(bottom = if (!isLast) 16.dp else 0.dp)
                            ) {
                                Text(status, color = PBTextDark,
                                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
                                Text(waktuDeskripsi, color = PBTextMid,
                                    lineHeight = 1.5.em,
                                    style = TextStyle(fontSize = 12.sp))
                            }
                        }
                    }
                }
            }
        }

        // ── BOTTOM BUTTON ─────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    spotColor = Color(0xFF3A5A9E).copy(alpha = 0.08f))
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color.White)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(PBPrimaryDk)
                    .shadow(8.dp, RoundedCornerShape(14.dp),
                        spotColor = PBPrimary.copy(alpha = 0.3f))
                    .clickable { onSelesai() }
                    .padding(vertical = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("✅", style = TextStyle(fontSize = 16.sp))
                    Text(
                        "Pesanan Selesai\"",
                        color = Color.White,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 390, heightDp = 900)
@Composable
fun PesananBerjalanPreview() {
    PesananBerjalanScreen()
}