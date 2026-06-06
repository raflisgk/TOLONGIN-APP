package com.example.tolongin.screens

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tolongin.PesananModel
import com.example.tolongin.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ── Warna Bawaan Anda ────────────────────────────────────────────────────────
private val DPrimary    = Color(0xFF1A4FAE)
private val DPrimaryDark= Color(0xFF0D3585)
private val DBgPage     = Color(0xFFFFFFFF)
private val DBgCard     = Color(0xFFF9FAFB)
private val DTextDark   = Color(0xFF111827)
private val DTextMid    = Color(0xFF6B7280)
private val DTextLight  = Color(0xFF9CA3AF)
private val DBorderCol  = Color(0xFFE5E7EB)
private val DBadgeBg    = Color(0xFFF3F4F6)

@Composable
fun DaftarPesananScreen(navController: NavHostController) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    var tabAktif by remember { mutableStateOf("Tersedia") }
    val tabList = listOf("Tersedia", "Berjalan", "Selesai")

    // State penampung data dari database Laragon
    var listPesananDb by remember { mutableStateOf<List<PesananModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // ── KONEKSI DATABASE VIA RETROFIT ──
    LaunchedEffect(Unit) {
        if (isPreview) {
            listPesananDb = listOf(
                PesananModel("TRX-260882", "raflisgk@gmail.com", "Titip Beli (kue)", "Hari ini, Segera", "Mencari Helper", "Rp 31.000"),
                PesananModel("TRX-441122", "raflisgk@gmail.com", "Titip Barang (Paket Kecil)", "Sekarang", "Mencari Helper", "Rp 13.000")
            )
            isLoading = false
        } else {
            RetrofitClient.instance.getPesanan("raflisgk@gmail.com").enqueue(object : Callback<List<PesananModel>> {
                override fun onResponse(call: Call<List<PesananModel>>, response: Response<List<PesananModel>>) {
                    isLoading = false
                    if (response.isSuccessful && response.body() != null) {
                        listPesananDb = response.body()!!
                    }
                }
                override fun onFailure(call: Call<List<PesananModel>>, t: Throwable) {
                    isLoading = false
                    Toast.makeText(context, "Gagal terhubung ke Laragon: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // ── FILTER DATA BERDASARKAN STATUS TAB ──
    val pesananTerfilter = listPesananDb.filter { item ->
        when (tabAktif) {
            "Tersedia" -> item.status.contains("Mencari", ignoreCase = true) || item.status.contains("Lunas", ignoreCase = true)
            "Berjalan" -> item.status.contains("Progres", ignoreCase = true) || item.status.contains("Jalan", ignoreCase = true)
            "Selesai"  -> item.status.contains("Selesai", ignoreCase = true)
            else       -> true
        }
    }

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
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DPrimary)
                    ) {
                        Text("T", color = Color.White, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.ExtraBold))
                    }

                    Text(
                        "Tolong.in",
                        color = DPrimary,
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    )

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

            // ── TAB TOGGLE ────────────────────────────────────────────────
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

            // ── LIST DATA PESANAN DARI DATABASE VIA RETROFIT ──────────────
            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = DPrimary)
                    }
                }
            } else if (pesananTerfilter.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                        Text("Tidak ada pesanan kategori ini.", color = DTextMid, fontSize = 14.sp)
                    }
                }
            } else {
                items(pesananTerfilter.size) { index ->
                    val item = pesananTerfilter[index]

                    PesananCardItem(
                        item = item,
                        tabAktif = tabAktif, // <-- Kirim data tab aktif ke card
                        onCardClick = {
                            navController.navigate("detail_pesanan_mitra/${item.id_transaksi}")
                        },
                        onAksiClick = {
                            if (tabAktif == "Tersedia") {
                                // Ambil Pesanan -> Ubah status jadi Progres
                                listPesananDb = listPesananDb.map { order ->
                                    if (order.id_transaksi == item.id_transaksi) order.copy(status = "Progres") else order
                                }
                                Toast.makeText(context, "Pesanan diambil! Masuk ke tab Berjalan.", Toast.LENGTH_SHORT).show()
                            } else if (tabAktif == "Berjalan") {
                                // Selesaikan Pesanan -> Ubah status jadi Selesai
                                listPesananDb = listPesananDb.map { order ->
                                    if (order.id_transaksi == item.id_transaksi) order.copy(status = "Selesai") else order
                                }
                                Toast.makeText(context, "Selamat! Pesanan telah selesai dikerjakan 🎉", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
            }

            item { Spacer(Modifier.height(8.dp)) }
        }

        // ── BOTTOM NAV NAVIGATION BAR ──────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp), spotColor = Color(0xFF3A5A9E).copy(alpha = 0.08f))
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color.White)
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                DNavItem(icon = "🏠", label = "Beranda", aktif = false, onClick = {
                    navController.navigate("beranda_helper") {
                        popUpTo("beranda_helper") { saveState = true }
                        launchSingleTop = true
                    }
                })
                DNavItem(icon = "📋", label = "Pesanan", aktif = true, onClick = {})
                DNavItem(icon = "📊", label = "Laporan", aktif = false, onClick = {})
                DNavItem(icon = "👤", label = "Profil", aktif = false, onClick = {})
            }
        }
    }
}

@Composable
private fun PesananCardItem(
    item: PesananModel,
    tabAktif: String,
    onCardClick: () -> Unit,
    onAksiClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val kategoriTampil = if (item.nama_layanan.contains("Clean", ignoreCase = true) || item.nama_layanan.contains("Bersih", ignoreCase = true)) "Kebersihan"
    else if (item.nama_layanan.contains("Beli", ignoreCase = true) || item.nama_layanan.contains("Titip", ignoreCase = true)) "Kurir"
    else "Perbaikan"

    val badgeIcon = when (kategoriTampil) {
        "Kebersihan" -> "🧹"
        "Kurir"      -> "🚚"
        else         -> "📦"
    }

    val deskripsiDummy = "Menerima layanan ${item.nama_layanan} terkonfirmasi resmi dari sistem aplikasi pelanggan Tolong.in."

    // ====================================================================
    // 📊 LOGIKA STRUKTUR BUTTON DINAMIS BERDASARKAN POSISI TAB MAS
    // ====================================================================
    val teksTombol = when (tabAktif) {
        "Tersedia" -> "Ambil Pesanan"
        "Berjalan" -> "Selesaikan Pesanan"
        else       -> "Pesanan Selesai"
    }

    val ikonTombol = when (tabAktif) {
        "Tersedia" -> "✋"
        "Berjalan" -> "✅"
        else       -> "🎉"
    }

    val warnaTombol = when (tabAktif) {
        "Tersedia" -> DPrimaryDark // Biru gelap bawaan mas
        "Berjalan" -> Color(0xFF10B981) // Hijau sukses premium
        else       -> Color(0xFF9CA3AF) // Abu-abu terkunci
    }

    val tombolBisaDiklik = tabAktif != "Selesai"
    // ====================================================================

    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = Color(0xFF3A5A9E).copy(alpha = 0.07f))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable { onCardClick() }
            .padding(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(DBadgeBg)
                    .border(1.dp, DBorderCol, RoundedCornerShape(9999.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(badgeIcon, style = TextStyle(fontSize = 11.sp))
                Text(kategoriTampil, color = DTextMid, style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium))
            }

            Text(item.total_harga, color = DPrimary, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.ExtraBold))
        }

        Text(item.nama_layanan, color = DTextDark, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold), modifier = Modifier.padding(bottom = 6.dp))
        Text(deskripsiDummy, color = DTextMid, lineHeight = 1.5.em, style = TextStyle(fontSize = 13.sp), modifier = Modifier.padding(bottom = 14.dp))

        HorizontalDivider(color = DBorderCol, modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("📍", style = TextStyle(fontSize = 12.sp))
                Text("1.2 km dari Anda", color = DTextMid, style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("🕐", style = TextStyle(fontSize = 12.sp))
                Text(item.tanggal, color = DTextMid, style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium))
            }
        }

        // Area Render Tombol Pintar Multi-Fungsi
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(warnaTombol)
                .clickable(enabled = tombolBisaDiklik) { onAksiClick() }
                .padding(vertical = 14.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(ikonTombol, style = TextStyle(fontSize = 16.sp))
                Text(teksTombol, color = Color.White, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))
            }
        }
    }
}

@Composable
private fun DNavItem(icon: String, label: String, aktif: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
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
        Text(label, color = if (aktif) DPrimary else DTextLight, style = TextStyle(fontSize = 11.sp, fontWeight = if (aktif) FontWeight.SemiBold else FontWeight.Normal))
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
fun DaftarPesananPreview() {
    DaftarPesananScreen(navController = rememberNavController())
}