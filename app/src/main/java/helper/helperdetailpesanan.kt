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
private val DPPrimary     = Color(0xFF1A4FAE)
private val DPPrimaryDark = Color(0xFF0D3585)
private val DPBgPage      = Color(0xFFF7F8FC)
private val DPTextDark    = Color(0xFF111827)
private val DPTextMid     = Color(0xFF6B7280)
private val DPTextLight   = Color(0xFF9CA3AF)
private val DPBorderCol   = Color(0xFFE5E7EB)
private val DPRedLight    = Color(0xFFFFF1F1)
private val DPRedText     = Color(0xFFDC2626)

@Composable
fun DetailPesananMitraScreen(
    onBack: () -> Unit = {},
    onTolak: () -> Unit = {},
    onTerima: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DPBgPage)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 96.dp)
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
                    // Tombol back kiri
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF3F4F6))
                            .clickable { onBack() }
                    ) {
                        Text("←", color = DPPrimary,
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
                    }

                    // Judul tengah
                    Text(
                        "Detail Pesanan",
                        color = DPPrimary,
                        style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // ── BADGE + ID ────────────────────────────────────────────────
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    // Badge Pesanan Baru
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(Color(0xFFE0EAFF))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            "Pesanan Baru",
                            color = DPPrimary,
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        )
                    }

                    // ID Pesanan
                    Text(
                        "ID: #ORD-9921",
                        color = DPTextMid,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    )
                }
            }

            // ── INFO PESANAN CARD ─────────────────────────────────────────
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .shadow(8.dp, RoundedCornerShape(20.dp),
                            spotColor = Color(0xFF3A5A9E).copy(alpha = 0.07f))
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                ) {
                    // ── Bagian atas: Avatar + Nama + Harga
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(DPPrimary)
                            ) {
                                Text(
                                    "A",
                                    color = Color.White,
                                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                                )
                            }

                            // Nama + pelanggan
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    "Jasa",
                                    color = DPTextDark,
                                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                                )
                                Text(
                                    "Perbaikan AC",
                                    color = DPTextDark,
                                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                                )
                                Text(
                                    "Bpk. Agus S.",
                                    color = DPTextMid,
                                    style = TextStyle(fontSize = 13.sp)
                                )
                            }
                        }

                        // Harga + label
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                "Rp",
                                color = DPPrimary,
                                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                            )
                            Text(
                                "150.000",
                                color = DPPrimary,
                                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                            )
                            Text(
                                "Estimasi",
                                color = DPTextLight,
                                style = TextStyle(fontSize = 10.sp)
                            )
                            Text(
                                "Pendapatan",
                                color = DPTextLight,
                                style = TextStyle(fontSize = 10.sp)
                            )
                        }
                    }

                    HorizontalDivider(color = DPBorderCol, modifier = Modifier.padding(horizontal = 20.dp))

                    // ── Bagian bawah: Detail info
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Alamat
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF0F4FF))
                            ) {
                                Text("📍", style = TextStyle(fontSize = 16.sp))
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text(
                                    "Alamat Pelanggan",
                                    color = DPTextDark,
                                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    "Jl. Sudirman No. 45, Komplek Mawar Blok C2, Kebayoran Baru, Jakarta Selatan",
                                    color = DPTextMid,
                                    lineHeight = 1.5.em,
                                    style = TextStyle(fontSize = 12.sp)
                                )
                            }
                        }

                        // Jarak & Waktu
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF0F4FF))
                            ) {
                                Text("🕐", style = TextStyle(fontSize = 16.sp))
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text(
                                    "Jarak & Waktu",
                                    color = DPTextDark,
                                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    "3.2 km • Est. 15 menit perjalanan",
                                    color = DPTextMid,
                                    style = TextStyle(fontSize = 12.sp)
                                )
                            }
                        }

                        // Catatan
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF0F4FF))
                            ) {
                                Text("📝", style = TextStyle(fontSize = 16.sp))
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text(
                                    "Catatan Pelanggan",
                                    color = DPTextDark,
                                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    "AC kurang dingin dan netes air. Tolong bawa tangga sendiri ya mas, di rumah tidak ada.",
                                    color = DPTextMid,
                                    lineHeight = 1.5.em,
                                    style = TextStyle(fontSize = 12.sp)
                                )
                            }
                        }
                    }
                }
            }

            // ── LOKASI PEKERJAAN ──────────────────────────────────────────
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        "Lokasi Pekerjaan",
                        color = DPTextDark,
                        style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                    )

                    // Peta placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .shadow(8.dp, RoundedCornerShape(20.dp),
                                spotColor = Color(0xFF3A5A9E).copy(alpha = 0.08f))
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(
                                    0f to Color(0xFF4A8FBF),
                                    0.5f to Color(0xFF6BAED4),
                                    1f to Color(0xFF3A7AAE),
                                    start = Offset(0f, 0f),
                                    end = Offset(400f, 200f)
                                )
                            )
                    ) {
                        // Simulasi jalan
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .fillMaxHeight()
                                .align(Alignment.Center)
                                .background(Color.White.copy(alpha = 0.3f))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .align(Alignment.Center)
                                .background(Color.White.copy(alpha = 0.3f))
                        )

                        // Gradient bawah
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .align(Alignment.BottomStart)
                                .background(
                                    Brush.verticalGradient(
                                        0f to Color.Transparent,
                                        1f to Color(0xFF2A6090).copy(alpha = 0.6f)
                                    )
                                )
                        )

                        // Pin marker
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            // Bubble "Buka Peta"
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .shadow(8.dp, RoundedCornerShape(9999.dp))
                                    .clip(RoundedCornerShape(9999.dp))
                                    .background(Color.White)
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("📍", style = TextStyle(fontSize = 13.sp))
                                    Text(
                                        "Buka Peta",
                                        color = DPPrimary,
                                        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    )
                                }
                            }
                            // Segitiga bawah bubble
                            Box(
                                modifier = Modifier
                                    .size(width = 12.dp, height = 8.dp)
                                    .background(Color.White,
                                        RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp))
                            )
                            // Tiang pin
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(16.dp)
                                    .background(Color.White.copy(alpha = 0.8f))
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }
        }

        // ── BOTTOM BUTTONS ────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .shadow(20.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    spotColor = Color(0xFF3A5A9E).copy(alpha = 0.1f))
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color.White)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Tombol Tolak
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(DPRedLight)
                        .border(1.dp, DPRedText.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                        .clickable { onTolak() }
                        .padding(vertical = 15.dp)
                ) {
                    Text(
                        "Tolak",
                        color = DPRedText,
                        style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    )
                }

                // Tombol Terima Pesanan
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(2.5f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(DPPrimaryDark)
                        .clickable { onTerima() }
                        .padding(vertical = 15.dp)
                ) {
                    Text(
                        "Terima Pesanan",
                        color = Color.White,
                        style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
fun DetailPesananMitraPreview() {
    DetailPesananMitraScreen()
}