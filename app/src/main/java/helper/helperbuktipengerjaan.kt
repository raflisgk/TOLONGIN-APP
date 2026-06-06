package com.example.tolongin.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Warna UI ────────────────────────────────────────────────────────
private val DPrimary = Color(0xFF004A99)
private val DBg      = Color(0xFFF9FAFB)

@Composable
fun LaporanPenyelesaianScreen(onBack: () -> Unit = {}) {
    var catatan by remember { mutableStateOf("") }
    var setuju by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(20.dp)
    ) {
        // ── TOP BAR ──
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
            Text("←", fontSize = 24.sp, modifier = Modifier.clickable { onBack() })
            Text("Laporan Penyelesaian", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp))
        }

        // ── RINCIAN ORDER ──
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = DBg),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFE0E7FF)), contentAlignment = Alignment.Center) {
                    Text("📋", fontSize = 20.sp)
                }
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text("Rincian Order", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Order #12345 - Pemasangan AC", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Upload Bukti Foto", fontWeight = FontWeight.Bold)

        // ── AREA UPLOAD FOTO (Diberi Modifier weight agar seimbang) ──
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            UploadBox("Foto Sebelum", "Kondisi awal", Modifier.weight(1f))
            UploadBox("Foto Sesudah", "Hasil pengerjaan", Modifier.weight(1f))
        }

        // ── CATATAN ──
        Text("Catatan Penyelesaian", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 10.dp))
        OutlinedTextField(
            value = catatan,
            onValueChange = { catatan = it },
            placeholder = { Text("Tulis rincian pengerjaan...", fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth().height(120.dp).padding(top = 8.dp),
            shape = RoundedCornerShape(12.dp)
        )

        // ── PERNYATAAN ──
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 16.dp)) {
            Checkbox(checked = setuju, onCheckedChange = { setuju = it })
            Text("Saya menyatakan bahwa data ini sesuai kondisi sebenarnya.", fontSize = 11.sp, color = Color.Gray)
        }

        // ── TOMBOL KIRIM ──
        Button(
            onClick = { Toast.makeText(context, "Laporan Terkirim!", Toast.LENGTH_SHORT).show() },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            enabled = setuju,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DPrimary)
        ) {
            Text("Kirim Laporan ➢", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun UploadBox(label: String, subLabel: String, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        val stroke = Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .drawBehind { drawRect(color = Color.Gray, style = stroke) }
                .background(DBg)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Text("📷\nTambah Foto", textAlign = TextAlign.Center, color = Color.Gray, fontSize = 12.sp)
        }
        Text(label, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        Text(subLabel, fontSize = 10.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun LaporanPenyelesaianPreview() {
    MaterialTheme {
        LaporanPenyelesaianScreen(onBack = {})
    }
}