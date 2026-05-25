package com.example.tolongin.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.tolongin.viewmodel.PesananViewModel

private val BgPage     = Color(0xFFF0F2FA)
private val Primary    = Color(0xFF005AB2)
private val TextDark   = Color(0xFF212D51)
private val TextMid    = Color(0xFF4E5A81)
private val TextLight  = Color(0xFF6A759E)
private val BorderColor = Color(0xFFE8EAF6)
private val InputBg    = Color(0xFFFAFBFF)
private val DividerCol = Color(0xFFEFF0FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPengirimanScreen(
    viewModel: PesananViewModel,
    onLanjut: () -> Unit
) {
    val pesanan by viewModel.pesanan.collectAsState()

    var lokasiJemput      by remember { mutableStateOf(pesanan.lokasiJemput.ifEmpty { "Apartemen Kemang Village, Tower Ritz" }) }
    var lokasiTujuan      by remember { mutableStateOf(pesanan.lokasiTujuan.ifEmpty { "Pakuwon Tower, Lt. 12, Tebet" }) }
    var jenisPaketDipilih by remember { mutableStateOf(pesanan.jenisPaket) }
    var deskripsi         by remember { mutableStateOf(pesanan.deskripsiBarang) }
    var waktu             by remember { mutableStateOf(pesanan.waktuJemput) }
    var namaPenerima      by remember { mutableStateOf(pesanan.namaPenerima) }
    var nomorHP           by remember { mutableStateOf(pesanan.nomorHP) }
    var catatan           by remember { mutableStateOf(pesanan.catatanHelper) }

    data class Paket(val nama: String, val berat: String, val icon: String)
    val paketList = listOf(
        Paket("Paket Kecil",  "MAKS 5 KG",        "📦"),
        Paket("Paket Sedang", "5-15 KG",           "🗃"),
        Paket("Barang Besar", "15-50 KG",          "🚛"),
        Paket("Fragile",      "PERLU BUBBLE WRAP", "🫙")
    )

    Box(modifier = Modifier.fillMaxSize().background(BgPage)) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(28.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 88.dp, bottom = 110.dp)
        ) {

            // ── ITEM 1: Lokasi ───────────────────────────────────────────
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(16.dp, RoundedCornerShape(24.dp),
                            ambientColor = Color(0xFF3A5A9E).copy(alpha = 0.06f),
                            spotColor   = Color(0xFF3A5A9E).copy(alpha = 0.08f))
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .padding(20.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Dot + garis vertikal
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(Color(0xFF10B981))
                            )
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(52.dp)
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color(0xFFA0ACD7), Color(0xFFA0ACD7))
                                        )
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(Color(0xFFB31B25))
                            )
                        }

                        // Input lokasi
                        Column(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Lokasi Jemput
                            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text(
                                    text = "LOKASI PENJEMPUTAN",
                                    color = TextLight,
                                    style = TextStyle(fontSize = 10.sp, letterSpacing = 0.8.sp)
                                )
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    BasicTextField(
                                        value = lokasiJemput,
                                        onValueChange = { lokasiJemput = it },
                                        textStyle = TextStyle(
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextDark,
                                            lineHeight = 1.4.em
                                        ),
                                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                                    )
                                    Text("⇅", color = Color(0xFF4493FF),
                                        style = TextStyle(fontSize = 18.sp))
                                }
                            }

                            HorizontalDivider(color = DividerCol)

                            // Lokasi Tujuan
                            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text(
                                    text = "TUJUAN PENGIRIMAN",
                                    color = TextLight,
                                    style = TextStyle(fontSize = 10.sp, letterSpacing = 0.8.sp)
                                )
                                BasicTextField(
                                    value = lokasiTujuan,
                                    onValueChange = { lokasiTujuan = it },
                                    textStyle = TextStyle(
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextDark,
                                        lineHeight = 1.4.em
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    // Tambah pemberhentian
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⊕", color = Primary, style = TextStyle(fontSize = 16.sp))
                        Text(
                            "Tambah pemberhentian",
                            color = Primary,
                            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }

            // ── ITEM 2: Jenis Paket ──────────────────────────────────────
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "JENIS PAKET", color = TextMid,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
                    )
                    paketList.chunked(2).forEach { baris ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            baris.forEach { paket ->
                                val dipilih = jenisPaketDipilih == paket.nama
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .shadow(
                                            if (dipilih) 8.dp else 3.dp,
                                            RoundedCornerShape(20.dp),
                                            spotColor = if (dipilih) Primary.copy(alpha = 0.25f)
                                            else Color(0xFF3A5A9E).copy(alpha = 0.05f)
                                        )
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(if (dipilih) Primary else Color.White)
                                        .clickable { jenisPaketDipilih = paket.nama }
                                        .padding(18.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Text(paket.icon, style = TextStyle(fontSize = 20.sp))
                                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Text(
                                                paket.nama,
                                                color = if (dipilih) Color.White else TextDark,
                                                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                paket.berat,
                                                color = if (dipilih) Color.White.copy(alpha = 0.75f) else TextMid,
                                                style = TextStyle(fontSize = 9.sp, letterSpacing = 0.5.sp)
                                            )
                                        }
                                    }
                                }
                            }
                            // Isi sisa kolom jika baris terakhir ganjil
                            if (baris.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            // ── ITEM 3: Detail Barang ────────────────────────────────────
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "DETAIL BARANG", color = TextMid,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
                    )
                    Text(
                        "Deskripsi Barang",
                        color = Color(0xFFA0ACD7),
                        style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                            .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 48.dp)
                    ) {
                        if (deskripsi.isEmpty()) {
                            Text(
                                "Contoh: Dokumen penting, kue ulang tahun...",
                                color = TextLight,
                                style = TextStyle(fontSize = 14.sp, lineHeight = 1.5.em)
                            )
                        }
                        BasicTextField(
                            value = deskripsi,
                            onValueChange = { deskripsi = it },
                            textStyle = TextStyle(fontSize = 14.sp, color = TextDark, lineHeight = 1.5.em),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // ── ITEM 4: Waktu Penjemputan ────────────────────────────────
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "WAKTU PENJEMPUTAN", color = TextMid,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(16.dp),
                                ambientColor = Color(0xFF3A5A9E).copy(alpha = 0.05f))
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 15.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(waktu, color = TextDark, style = TextStyle(fontSize = 15.sp))
                            Text("⌄", color = Primary, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }

            // ── ITEM 5: Penerima ─────────────────────────────────────────
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "PENERIMA", color = TextMid,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
                    )
                    // Nama
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(InputBg)
                            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        if (namaPenerima.isEmpty()) {
                            Text("Nama Penerima", color = Color(0xFF6B7280), style = TextStyle(fontSize = 15.sp))
                        }
                        BasicTextField(
                            value = namaPenerima,
                            onValueChange = { namaPenerima = it },
                            textStyle = TextStyle(fontSize = 15.sp, color = TextDark),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    // No HP
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(InputBg)
                            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        if (nomorHP.isEmpty()) {
                            Text("No. HP Penerima", color = Color(0xFF6B7280), style = TextStyle(fontSize = 15.sp))
                        }
                        BasicTextField(
                            value = nomorHP,
                            onValueChange = { nomorHP = it },
                            textStyle = TextStyle(fontSize = 15.sp, color = TextDark),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // ── ITEM 6: Catatan Helper ───────────────────────────────────
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "CATATAN UNTUK HELPER", color = TextMid,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                            .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 48.dp)
                    ) {
                        if (catatan.isEmpty()) {
                            Text(
                                "Titip ke resepsionis jika saya tidak di tempat...",
                                color = Color(0xFF6B7280),
                                style = TextStyle(fontSize = 14.sp, lineHeight = 1.5.em)
                            )
                        }
                        BasicTextField(
                            value = catatan,
                            onValueChange = { catatan = it },
                            textStyle = TextStyle(fontSize = 14.sp, color = TextDark),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // ── ITEM 7: Helper Terpilih ──────────────────────────────────
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "HELPER TERPILIH", color = TextMid,
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
                        )
                        Text(
                            "Lihat helper lain", color = Primary,
                            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(12.dp, RoundedCornerShape(24.dp),
                                ambientColor = Color(0xFF3A5A9E).copy(alpha = 0.07f),
                                spotColor   = Color(0xFF3A5A9E).copy(alpha = 0.1f))
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Avatar
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(9999.dp))
                                    .background(Color(0xFF8FA8C8))
                            ) {
                                Text("BD", color = Color.White,
                                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        pesanan.helperName, color = TextDark,
                                        style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                    )
                                    Text("★", color = Color(0xFFF59E0B), style = TextStyle(fontSize = 10.sp))
                                    Text(
                                        pesanan.helperRating, color = Color(0xFFF59E0B),
                                        style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    )
                                }
                                Text(
                                    pesanan.helperSpesialis, color = TextMid,
                                    style = TextStyle(fontSize = 11.sp)
                                )
                                Spacer(Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        "1.2 km dari Anda", color = Color(0xFF059669),
                                        style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        "✓ Verified", color = Color(0xFF2563EB),
                                        style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                            Text("›", color = Color(0xFFCBD5E1), style = TextStyle(fontSize = 22.sp))
                        }
                    }
                }
            }

            // ── ITEM 8: Ringkasan Biaya ──────────────────────────────────
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFFDEEAFF))
                        .padding(20.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "RINGKASAN BIAYA", color = Primary,
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(9999.dp))
                                .background(Color.White)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "Jarak: ${pesanan.jarak} km", color = Primary,
                                style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Biaya Pengiriman", color = TextMid, style = TextStyle(fontSize = 13.sp))
                            Text("Rp ${"%,d".format(pesanan.biayaPengiriman).replace(',', '.')}",
                                color = TextDark, style = TextStyle(fontSize = 13.sp))
                        }
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Asuransi (Proteksi Barang)", color = TextMid, style = TextStyle(fontSize = 13.sp))
                            Text("Rp ${"%,d".format(pesanan.biayaAsuransi).replace(',', '.')}",
                                color = TextDark, style = TextStyle(fontSize = 13.sp))
                        }
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Biaya Platform", color = TextMid, style = TextStyle(fontSize = 13.sp))
                            Text("Rp ${"%,d".format(pesanan.biayaPlatform).replace(',', '.')}",
                                color = TextDark, style = TextStyle(fontSize = 13.sp))
                        }
                        HorizontalDivider(color = Primary.copy(alpha = 0.15f))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Total Pembayaran", color = TextDark,
                                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            )
                            Text(
                                pesanan.totalBayarFormatted, color = Primary,
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        // ── TOP APP BAR ──────────────────────────────────────────────────
        TopAppBar(
            title = {
                Text(
                    "Detail Pengiriman", color = TextDark,
                    style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.3).sp)
                )
            },
            navigationIcon = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(36.dp)
                        .clip(RoundedCornerShape(9999.dp))
                        .background(Color(0xFFEFF0FF))
                ) {
                    Text("←", color = Primary, style = TextStyle(fontSize = 16.sp))
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = BgPage),
            modifier = Modifier.align(Alignment.TopStart)
        )

        // ── BOTTOM BUTTON ────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0f to BgPage.copy(alpha = 0f),
                        0.25f to BgPage.copy(alpha = 0.95f),
                        1f to BgPage
                    )
                )
                .padding(start = 24.dp, end = 24.dp, bottom = 32.dp, top = 20.dp)
        ) {
            Button(
                onClick = {
                    viewModel.updateLokasi(lokasiJemput, lokasiTujuan)
                    viewModel.updateJenisPaket(
                        jenisPaketDipilih,
                        paketList.find { it.nama == jenisPaketDipilih }?.berat ?: ""
                    )
                    viewModel.updateDetailBarang(deskripsi)
                    viewModel.updateWaktu(waktu)
                    viewModel.updatePenerima(namaPenerima, nomorHP)
                    viewModel.updateCatatan(catatan)
                    onLanjut()
                },
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                modifier = Modifier.fillMaxWidth().height(54.dp)
            ) {
                Text(
                    "Lanjut ke Konfirmasi →", color = Color.White,
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Preview(widthDp = 390, heightDp = 1862)
@Composable
fun DetailPengirimanPreview() {
    val viewModel = PesananViewModel()
    DetailPengirimanScreen(viewModel = viewModel, onLanjut = {})
}