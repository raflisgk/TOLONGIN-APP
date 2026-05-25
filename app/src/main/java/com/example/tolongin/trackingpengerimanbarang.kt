package com.example.tolongin.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.tolongin.viewmodel.PesananViewModel

enum class StatusType { DONE, ACTIVE, PENDING }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingScreen(
    viewModel: PesananViewModel,
    onBack: () -> Unit
) {
    val p by viewModel.pesanan.collectAsState()
    val bgPage = Color(0xFFF0F2FA)
    val primary = Color(0xFF005AB2)
    val textDark = Color(0xFF212D51)
    val textMid = Color(0xFF4E5A81)

    Box(modifier = Modifier.fillMaxSize().background(bgPage)) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 96.dp, bottom = 48.dp)
        ) {
            // Hero card
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                        .shadow(32.dp, RoundedCornerShape(32.dp), spotColor = primary.copy(alpha = 0.3f))
                        .clip(RoundedCornerShape(32.dp))
                        .background(Brush.linearGradient(0f to primary, 1f to Color(0xFF64A1FF),
                            start = Offset(25f, -22f), end = Offset(317f, 317f)))
                        .padding(24.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clip(RoundedCornerShape(9999.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)), RoundedCornerShape(9999.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(9999.dp)).background(Color(0xFF22C55E)))
                            Text("DALAM\nPERJALANAN", color = Color.White, lineHeight = 1.3.em,
                                style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("ETA", color = Color.White.copy(alpha = 0.7f), style = TextStyle(fontSize = 13.sp))
                            Text("Tiba dalam 8\nmenit", color = Color.White, textAlign = TextAlign.End,
                                lineHeight = 1.3.em, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                        }
                    }
                    Text("Kurir menuju lokasi\ntujuan", color = Color.White, lineHeight = 1.25.em,
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold), modifier = Modifier.fillMaxWidth())
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        listOf(Pair("📞", "HUBUNGI"), Pair("💬", "CHAT"), Pair("🗺", "PETA"), Pair("✕", "BATALKAN"))
                            .forEach { (icon, label) ->
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(14.dp))
                                        .background(Color.White.copy(alpha = 0.15f)).padding(vertical = 10.dp)
                                ) {
                                    Text(icon, style = TextStyle(fontSize = 16.sp))
                                    Text(label, color = Color.White, textAlign = TextAlign.Center,
                                        style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold))
                                }
                            }
                    }
                }
            }

            // Peta + Lokasi Kurir
            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFFD0DCFF))
                    ) {
                        Box(modifier = Modifier.fillMaxSize().background(
                            Brush.verticalGradient(0f to Color.Transparent, 1f to Color(0xFFEFF0FF).copy(alpha = 0.8f))))
                        Box(contentAlignment = Alignment.Center,
                            modifier = Modifier.size(40.dp).align(Alignment.Center)
                                .clip(RoundedCornerShape(9999.dp)).background(primary)
                        ) { Text("🚴", style = TextStyle(fontSize = 18.sp)) }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                            .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = Color(0xFF3A5A9E).copy(alpha = 0.1f))
                            .clip(RoundedCornerShape(24.dp)).background(Color.White).padding(20.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center,
                            modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF64A1FF).copy(alpha = 0.15f))
                        ) { Text("📍", style = TextStyle(fontSize = 20.sp)) }
                        Column {
                            Text("LOKASI KURIR SAAT INI", color = textMid,
                                style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.6.sp))
                            Text("Jl. Kebon Jeruk No. 42, Jakarta Barat", color = textDark,
                                lineHeight = 1.4.em, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }

            // Helper
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp)).background(Color(0xFFEFF0FF)).padding(20.dp)
                ) {
                    Box(modifier = Modifier.size(56.dp)) {
                        Box(contentAlignment = Alignment.Center,
                            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFF8FA8C8))
                        ) { Text("BD", color = Color.White, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)) }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(Alignment.BottomEnd).offset(x = 4.dp, y = 4.dp)
                                .clip(RoundedCornerShape(9999.dp)).background(Color.White)
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text("★", color = Color(0xFFEAB308), style = TextStyle(fontSize = 8.sp))
                            Text(p.helperRating, color = textDark,
                                style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold))
                        }
                    }
                    Column {
                        Text(p.helperName, color = textDark,
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
                        Text("Helper Terverifikasi", color = textMid, style = TextStyle(fontSize = 13.sp))
                    }
                }
            }

            // Status Pesanan timeline
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    modifier = Modifier.fillMaxWidth()
                        .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = Color(0xFF3A5A9E).copy(alpha = 0.08f))
                        .clip(RoundedCornerShape(32.dp)).background(Color.White).padding(28.dp)
                ) {
                    Text("Status Pesanan", color = textDark,
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
                    Spacer(Modifier.height(20.dp))
                    val statuses = listOf(
                        Triple("Pesanan diterima", "14:20 WIB", StatusType.DONE),
                        Triple("Barang dijemput", "14:35 WIB", StatusType.DONE),
                        Triple("Dalam perjalanan", "Sedang menuju lokasi Anda", StatusType.ACTIVE),
                        Triple("Barang tiba", "Estimasi 14:55 WIB", StatusType.PENDING),
                        Triple("Selesai", "", StatusType.PENDING)
                    )
                    statuses.forEachIndexed { index, (title, subtitle, type) ->
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(contentAlignment = Alignment.Center,
                                    modifier = Modifier.size(22.dp).clip(RoundedCornerShape(9999.dp))
                                        .background(when (type) {
                                            StatusType.DONE -> primary.copy(alpha = 0.12f)
                                            StatusType.ACTIVE -> primary
                                            StatusType.PENDING -> Color(0xFFD3DCFF)
                                        })
                                ) {
                                    Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(9999.dp))
                                        .background(when (type) {
                                            StatusType.DONE -> primary
                                            StatusType.ACTIVE -> Color.White
                                            StatusType.PENDING -> Color(0xFFA0ACD7)
                                        }))
                                }
                                if (index < statuses.lastIndex) {
                                    Box(modifier = Modifier.width(2.dp).height(32.dp)
                                        .background(if (type == StatusType.DONE) primary.copy(alpha = 0.2f) else Color(0xFFD3DCFF)))
                                }
                            }
                            Column(modifier = Modifier.padding(top = 2.dp)) {
                                Text(title,
                                    color = when (type) {
                                        StatusType.DONE -> textDark
                                        StatusType.ACTIVE -> primary
                                        StatusType.PENDING -> Color(0xFF6A759E)
                                    },
                                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
                                if (subtitle.isNotEmpty())
                                    Text(subtitle,
                                        color = if (type == StatusType.ACTIVE) Color(0xFF4493FF) else textMid,
                                        style = TextStyle(fontSize = 12.sp))
                                Spacer(Modifier.height(if (index < statuses.lastIndex) 8.dp else 0.dp))
                            }
                        }
                    }
                }
            }

            // Detail Pengiriman
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFFEFF0FF))
                        .border(BorderStroke(1.dp, Color(0xFFA0ACD7).copy(alpha = 0.1f)), RoundedCornerShape(32.dp))
                        .padding(24.dp)
                ) {
                    Text("Detail Pengiriman", color = textDark,
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
                    listOf(
                        Pair("Item", if (p.deskripsiBarang.isNotEmpty()) p.deskripsiBarang else p.jenisPaket),
                        Pair("Penerima", if (p.namaPenerima.isNotEmpty()) p.namaPenerima else "–"),
                        Pair("Tujuan", if (p.lokasiTujuan.isNotEmpty()) p.lokasiTujuan else "–")
                    ).forEach { (label, value) ->
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(label, color = textMid, style = TextStyle(fontSize = 14.sp), modifier = Modifier.weight(0.4f))
                            Text(value, color = textDark, textAlign = TextAlign.End, lineHeight = 1.4.em,
                                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold), modifier = Modifier.weight(0.6f))
                        }
                    }
                    HorizontalDivider(color = Color(0xFFA0ACD7).copy(alpha = 0.2f))
                    Row(horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text("Total Pembayaran", color = textDark,
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
                        Text(p.totalBayarFormatted, color = primary,
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                    }
                }
            }

            // Order ID
            item {
                Text("ORDER ID: ${p.orderId}", color = Color(0xFF6A759E), textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
            }
        }

        // Top Bar
        TopAppBar(
            title = { Text("Tracking Pengiriman", color = textDark,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.4).sp)) },
            navigationIcon = {
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(start = 8.dp).size(36.dp)
                        .clip(RoundedCornerShape(9999.dp)).background(Color(0xFFEFF0FF))
                        .clickable { onBack() }
                ) { Text("←", color = primary, style = TextStyle(fontSize = 16.sp)) }
            },
            actions = {
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(end = 8.dp).size(36.dp)
                        .clip(RoundedCornerShape(9999.dp)).background(Color(0xFFEFF0FF))
                ) { Text("⋯", color = primary, style = TextStyle(fontSize = 16.sp)) }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = bgPage),
            modifier = Modifier.align(Alignment.TopStart)
        )
    }
}

@Preview(widthDp = 390, heightDp = 1916)
@Composable
private fun TrackingPreview() {
    val viewModel = PesananViewModel().apply {
        updateLokasi("Apartemen Kemang Village, Tower Ritz", "Pakuwon Tower, Lt. 12, Tebet")
        updateJenisPaket("Paket Kecil", "MAKS 5 KG")
        updatePenerima("Andini Putri", "08123456789")
        updateDetailBarang("Paket Dokumen & Laptop")
    }
    TrackingScreen(viewModel = viewModel, onBack = {})
}