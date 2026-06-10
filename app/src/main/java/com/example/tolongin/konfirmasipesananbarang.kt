package com.example.tolongin.screens

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.tolongin.RetrofitClient
import com.example.tolongin.viewmodel.PesananViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KonfirmasiScreen(
    viewModel: PesananViewModel,
    onBack: () -> Unit,
    onBayar: () -> Unit
) {
    // 1. Ambil context untuk SharedPreferences
    val context = LocalContext.current
    val p by viewModel.pesanan.collectAsState()

    val bgPage = Color(0xFFF0F2FA)
    val primary = Color(0xFF005AB2)
    val textDark = Color(0xFF212D51)
    val textMid = Color(0xFF4E5A81)

    Box(modifier = Modifier.fillMaxSize().background(bgPage)) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 72.dp, bottom = 100.dp)
        ) {
            // Progress jemput-tujuan
            item {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text("3.4 km • 12 mnt", color = primary,
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
                        Spacer(Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(999.dp)).background(Color(0xFF10B981)))
                                Spacer(Modifier.height(4.dp))
                                Text("JEMPUT", color = textMid, style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold))
                            }
                            Box(modifier = Modifier.weight(1f).height(2.dp).padding(horizontal = 4.dp).background(primary))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(999.dp)).background(Color(0xFFB31B25)))
                                Spacer(Modifier.height(4.dp))
                                Text("TUJUAN", color = textMid, style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }
            }

            // Detail Pesanan card
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text("Detail Pesanan", color = textDark,
                            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        Text("Lihat Peta", color = primary, style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold))
                    }

                    // Helper
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEFF0FF)).padding(12.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center,
                            modifier = Modifier.size(52.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF8FA8C8))
                        ) { Text("BD", color = Color.White, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)) }
                        Column {
                            Text("HELPER TERPILIH", color = textMid,
                                style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp))
                            Text(p.helperName, color = textDark,
                                style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold))
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("★", color = Color(0xFFF59E0B), style = TextStyle(fontSize = 10.sp))
                                Text("${p.helperRating} • 1.2rb Selesai", color = textMid,
                                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold))
                            }
                        }
                    }

                    // Detail Barang & Waktu
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("DETAIL BARANG", color = textMid,
                                style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp))
                            Spacer(Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("📦", style = TextStyle(fontSize = 12.sp))
                                Text(p.jenisPaket, color = textDark, style = TextStyle(fontSize = 13.sp))
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("WAKTU JEMPUT", color = textMid,
                                style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp))
                            Spacer(Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("🕐", style = TextStyle(fontSize = 12.sp))
                                Text(if (p.waktuJemput.contains("Sekarang")) "Sekarang" else p.waktuJemput,
                                    color = primary, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold))
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFFEFF0FF))

                    // Penerima
                    Column {
                        Text("PENERIMA", color = textMid,
                            style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp))
                        Spacer(Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("👤", style = TextStyle(fontSize = 12.sp))
                            Text(if (p.namaPenerima.isNotEmpty()) p.namaPenerima else "–", color = textDark, style = TextStyle(fontSize = 13.sp))
                            if (p.nomorHP.isNotEmpty())
                                Text("(${p.nomorHP})", color = textMid, style = TextStyle(fontSize = 12.sp))
                        }
                    }

                    // Catatan
                    if (p.catatanHelper.isNotEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFDBE1FF).copy(alpha = 0.4f)).padding(10.dp)
                        ) {
                            Column {
                                Text("CATATAN UNTUK HELPER", color = textMid,
                                    style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp))
                                Spacer(Modifier.height(2.dp))
                                Text("\u201c${p.catatanHelper}\u201d", color = textDark, style = TextStyle(fontSize = 13.sp))
                            }
                        }
                    }
                }
            }

            // Proteksi Barang
            item {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White).padding(16.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Box(contentAlignment = Alignment.Center,
                            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF64A1FF).copy(alpha = 0.15f))
                        ) { Text("🛡", style = TextStyle(fontSize = 22.sp)) }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("Proteksi Barang", color = textDark,
                                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))
                                Text("Rp ${"%,d".format(p.biayaAsuransi).replace(',', '.')}",
                                    color = primary, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold))
                            }
                            Text(buildAnnotatedString {
                                withStyle(SpanStyle(color = textMid, fontSize = 11.sp)) { append("Nilai barang hingga ") }
                                withStyle(SpanStyle(color = textMid, fontSize = 11.sp, fontWeight = FontWeight.Bold)) { append("Rp 500.000") }
                                withStyle(SpanStyle(color = textMid, fontSize = 11.sp)) { append(" dengan perlindungan menyeluruh.") }
                            })
                        }
                    }
                }
            }

            // Rincian Biaya
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEFF0FF)).padding(20.dp)
                ) {
                    // Wallet row
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)).background(Color.White)
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(contentAlignment = Alignment.Center,
                                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp))
                                    .background(primary.copy(alpha = 0.1f))
                            ) { Text("💳", style = TextStyle(fontSize = 16.sp)) }
                            Column {
                                Text("TOLONG WALLET", color = primary,
                                    style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp))
                                Text("Rp 50.000", color = textDark,
                                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))
                            }
                        }
                        Text("Ganti", color = primary, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold))
                    }

                    // Biaya detail
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Ongkir (${p.jarak} km)", color = textMid, style = TextStyle(fontSize = 13.sp))
                            Text("Rp ${"%,d".format(p.biayaPengiriman).replace(',', '.')}", color = textDark, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium))
                        }
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Proteksi Premi", color = textMid, style = TextStyle(fontSize = 13.sp))
                            Text("Rp ${"%,d".format(p.biayaAsuransi).replace(',', '.')}", color = textDark, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium))
                        }
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Biaya Layanan", color = textMid, style = TextStyle(fontSize = 13.sp))
                            Text("Rp ${"%,d".format(p.biayaPlatform).replace(',', '.')}", color = textDark, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium))
                        }
                        HorizontalDivider(color = primary.copy(alpha = 0.1f))
                        Row(horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text("Total Bayar", color = textDark,
                                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
                            Text(p.totalBayarFormatted, color = primary,
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }

            // Disclaimer
            item {
                Text(
                    text = "Dengan menekan tombol bayar, Anda menyetujui Syarat & Ketentuan serta kebijakan penggunaan asuransi yang berlaku di platform Tolong.in.",
                    color = textMid.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    lineHeight = 1.6.em,
                    style = TextStyle(fontSize = 10.sp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }
        }

        // Top Bar
        TopAppBar(
            title = { Text("Konfirmasi Pesanan", color = primary,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)) },
            navigationIcon = {
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(start = 8.dp).size(36.dp)
                        .clip(RoundedCornerShape(9999.dp)).background(Color(0xFFEFF0FF))
                ) { Text("←", color = primary, style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.clickable { onBack() }) }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = bgPage),
            modifier = Modifier.align(Alignment.TopStart)
        )

        // Bottom Button
        Box(
            modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth()
                .background(Color.White.copy(alpha = 0.95f))
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Button(
                // ====================================================================
                // LOGIKA ALUR PENGIRIMAN: SIMPAN KE SHAREDPREFS SEBELUM PINDAH KE QRIS
                // ====================================================================
                onClick = {
                    val idTrxAcak = "TRX-${(100000..999999).random()}"

                    val sharedPreferences = context.getSharedPreferences("TolonginPref", Context.MODE_PRIVATE)
                    val emailLogin = sharedPreferences.getString("USER_EMAIL", "") ?: ""

                    // Ambil list pesanan lama
                    val pesananLama = sharedPreferences.getString("LIST_PESANAN", "[]") ?: "[]"
                    val listJson = org.json.JSONArray(pesananLama)

// Buat objek pesanan baru
                    val obj = org.json.JSONObject().apply {
                        put("id_transaksi", idTrxAcak)
                        put("email", emailLogin)
                        put("nama_layanan", "Titip Barang (${p.jenisPaket})")
                        put("tanggal", p.waktuJemput)
                        put("status", "Lunas")
                        put("total_harga", p.totalBayarFormatted)
                        put("lokasi_jemput", p.lokasiJemput)
                        put("lokasi_tujuan", p.lokasiTujuan)
                        put("jenis_paket", p.jenisPaket)
                        put("nama_penerima", p.namaPenerima)
                        put("no_hp_penerima", p.nomorHP)
                        put("catatan_helper", p.catatanHelper)
                    }
                    listJson.put(obj)

                    sharedPreferences.edit().apply {
                        putString("LIST_PESANAN", listJson.toString())
                        putString("HARGALAYANAN", p.totalBayarFormatted)
                        putString("NAMALAYANAN", "Titip Barang (${p.jenisPaket})")
                        putString("TRX_ID", idTrxAcak)
                        putString("SELECTED_DATE", p.waktuJemput)
                        apply()
                    }

                    // Kirim ke database
                    val body = okhttp3.FormBody.Builder()
                        .add("id_transaksi", idTrxAcak)
                        .add("email", emailLogin)
                        .add("nama_layanan", "Titip Barang (${p.jenisPaket})")
                        .add("tanggal", p.waktuJemput)
                        .add("status", "Lunas")
                        .add("total_harga", p.totalBayarFormatted)
                        .add("lokasi_jemput", p.lokasiJemput)
                        .add("lokasi_tujuan", p.lokasiTujuan)
                        .add("jenis_paket", p.jenisPaket)
                        .add("deskripsi_barang", p.deskripsiBarang)
                        .add("waktu_jemput", p.waktuJemput)
                        .add("nama_penerima", p.namaPenerima)
                        .add("no_hp_penerima", p.nomorHP)
                        .add("catatan_helper", p.catatanHelper)
                        .build()

                    // Taruh ini tepat sebelum val request = okhttp3.Request.Builder()
                    android.util.Log.d("DEBUG_BODY", "lokasi_jemput: ${p.lokasiJemput}")
                    android.util.Log.d("DEBUG_BODY", "lokasi_tujuan: ${p.lokasiTujuan}")
                    android.util.Log.d("DEBUG_BODY", "total_harga: ${p.totalBayarFormatted}")

                    val request = okhttp3.Request.Builder()
                        .url("http://192.168.0.107/tolongin_api/insert_titip_beli.php")
                        .post(body)
                        .build()

                    okhttp3.OkHttpClient().newCall(request).enqueue(object : okhttp3.Callback {
                        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                            // Opsional: Bisa diisi logika kalau internet mati/gagal konek
                        }
                        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                            android.util.Log.d("DEBUG_PHP", "Response: ${response.peekBody(Long.MAX_VALUE).string()}")
                            android.os.Handler(android.os.Looper.getMainLooper()).post {
                                viewModel.reset()
                                onBayar()
                            }
                        }
                    })
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primary),
                modifier = Modifier.fillMaxWidth().height(54.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("🔒", style = TextStyle(fontSize = 14.sp))
                    Text("Bayar Sekarang • ${p.totalBayarFormatted}", color = Color.White,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Preview(widthDp = 390, heightDp = 1279)
@Composable
private fun KonfirmasiPreview() {
    val viewModel = PesananViewModel().apply {
        updateLokasi("Apartemen Kemang Village, Tower Ritz", "Pakuwon Tower, Lt. 12, Tebet")
        updateJenisPaket("Paket Kecil", "MAKS 5 KG")
        updatePenerima("Siska", "08123456789")
        updateCatatan("Titip di pos satpam")
    }
    KonfirmasiScreen(viewModel = viewModel, onBack = {}, onBayar = {})
}