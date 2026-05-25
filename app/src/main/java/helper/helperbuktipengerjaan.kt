package com.example.tolongin.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
private val UPPrimary    = Color(0xFF1A4FAE)
private val UPBgPage     = Color(0xFFFFFFFF)
private val UPBgCard     = Color(0xFFF9FAFB)
private val UPTextDark   = Color(0xFF111827)
private val UPTextMid    = Color(0xFF6B7280)
private val UPTextLight  = Color(0xFF9CA3AF)
private val UPBorderCol  = Color(0xFFE5E7EB)

@Composable
fun UpdateProgressScreen2(
    onBack: () -> Unit = {},
    onSimpan: () -> Unit = {}
) {
    var statusDipilih by remember { mutableStateOf("Menuju Lokasi") }
    var catatan by remember { mutableStateOf("") }

    val statusList = listOf(
        "Menuju Lokasi",
        "Sudah Sampai",
        "Sedang Dikerjakan",
        "Hampir Selesai"
    )

    data class Riwayat(
        val waktu: String,
        val status: String,
        val deskripsi: String,
        val aktif: Boolean,
        val dalamCard: Boolean
    )

    val riwayatList = listOf(
        Riwayat(
            waktu = "Hari ini, 10:30 WIB",
            status = "Menuju Lokasi",
            deskripsi = "Sedang dalam perjalanan, perkiraan tiba 15 menit lagi. Jalanan agak macet di simpang tiga.",
            aktif = true,
            dalamCard = true
        ),
        Riwayat(
            waktu = "Hari ini, 09:00 WIB",
            status = "Pekerjaan Diterima",
            deskripsi = "Helper telah menerima permintaan tugas ini dan sedang mempersiapkan peralatan.",
            aktif = false,
            dalamCard = false
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UPBgPage)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp)
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
                    // Back button
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable { onBack() }
                    ) {
                        Text(
                            "←",
                            color = UPTextDark,
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                    // Judul
                    Text(
                        "Update Progress",
                        color = UPTextDark,
                        style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // ── STATUS PEKERJAAN ──────────────────────────────────────────
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 28.dp, bottom = 4.dp)
                ) {
                    Text(
                        "Status Pekerjaan",
                        color = UPTextDark,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    )

                    Spacer(Modifier.height(4.dp))

                    statusList.forEach { status ->
                        val dipilih = statusDipilih == status

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = UPBorderCol,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { statusDipilih = status }
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                        ) {
                            // Radio button
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = if (dipilih) 2.dp else 1.5.dp,
                                        color = if (dipilih) UPPrimary else Color(0xFFD1D5DB),
                                        shape = CircleShape
                                    )
                                    .background(Color.White)
                            ) {
                                if (dipilih) {
                                    Box(
                                        modifier = Modifier
                                            .size(11.dp)
                                            .clip(CircleShape)
                                            .background(UPPrimary)
                                    )
                                }
                            }

                            Text(
                                status,
                                color = UPTextDark,
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    fontWeight = if (dipilih) FontWeight.Medium else FontWeight.Normal
                                )
                            )
                        }
                    }
                }
            }

            // ── CATATAN TAMBAHAN ──────────────────────────────────────────
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 24.dp, bottom = 4.dp)
                ) {
                    Text(
                        "Catatan Tambahan",
                        color = UPTextDark,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .border(1.dp, UPBorderCol, RoundedCornerShape(10.dp))
                            .padding(16.dp)
                    ) {
                        if (catatan.isEmpty()) {
                            Text(
                                "Tulik catatan atau kendala di kini...",
                                color = UPTextLight,
                                style = TextStyle(fontSize = 14.sp, lineHeight = 1.5.em)
                            )
                        }
                        BasicTextField(
                            value = catatan,
                            onValueChange = { catatan = it },
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = UPTextDark,
                                lineHeight = 1.5.em
                            ),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // ── TOMBOL SIMPAN UPDATE ──────────────────────────────────────
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp, bottom = 28.dp)
                        .shadow(6.dp, RoundedCornerShape(12.dp),
                            spotColor = UPPrimary.copy(alpha = 0.25f))
                        .clip(RoundedCornerShape(12.dp))
                        .background(UPPrimary)
                        .clickable { onSimpan() }
                        .padding(vertical = 17.dp)
                ) {
                    Text(
                        "Simpan Update",
                        color = Color.White,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }

            // ── DIVIDER ───────────────────────────────────────────────────
            item {
                HorizontalDivider(
                    color = UPBorderCol,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 28.dp)
                )
            }

            // ── RIWAYAT UPDATE HEADING ────────────────────────────────────
            item {
                Text(
                    "Riwayat Update",
                    color = UPTextDark,
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp)
                )
            }

            // ── RIWAYAT ITEMS ─────────────────────────────────────────────
            items(riwayatList.size) { index ->
                val item = riwayatList[index]
                val isLast = index == riwayatList.lastIndex

                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    // Timeline: dot + garis
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(18.dp)
                    ) {
                        // Dot
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(
                                    if (item.aktif) UPPrimary
                                    else Color(0xFFD1D5DB)
                                )
                        ) {
                            if (item.aktif) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            }
                        }
                        // Garis vertikal
                        if (!isLast) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(if (item.dalamCard) 130.dp else 80.dp)
                                    .background(UPBorderCol)
                            )
                        }
                    }

                    // Konten
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = if (!isLast) 20.dp else 24.dp)
                    ) {
                        // Waktu
                        Text(
                            item.waktu,
                            color = UPTextLight,
                            style = TextStyle(fontSize = 11.sp)
                        )
                        // Status (bold)
                        Text(
                            item.status,
                            color = UPTextDark,
                            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                        )
                        // Deskripsi: dalam card atau teks biasa
                        if (item.dalamCard) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(UPBgCard)
                                    .border(1.dp, UPBorderCol, RoundedCornerShape(10.dp))
                                    .padding(14.dp)
                            ) {
                                Text(
                                    item.deskripsi,
                                    color = UPTextMid,
                                    lineHeight = 1.55.em,
                                    style = TextStyle(fontSize = 13.sp)
                                )
                            }
                        } else {
                            Text(
                                item.deskripsi,
                                color = UPTextMid,
                                lineHeight = 1.55.em,
                                style = TextStyle(fontSize = 13.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(widthDp = 390, heightDp = 900)
@Composable
fun UpdateProgressScreen2Preview() {
    UpdateProgressScreen2()
}