package com.example.tolongin.screens

import android.net.Uri
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.tolongin.ResponseModel
import com.example.tolongin.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val DPrimary     = Color(0xFF1A4FAE)
private val DPrimaryDark = Color(0xFF0D3585)
private val DBgPage      = Color(0xFFFFFFFF)
private val DBgCard      = Color(0xFFF9FAFB)
private val DTextDark    = Color(0xFF111827)
private val DTextMid     = Color(0xFF6B7280)
private val DBorderCol   = Color(0xFFE5E7EB)
private val DBadgeBg     = Color(0xFFF3F4F6)

@Composable
fun DaftarPesananScreen(navController: NavHostController) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    var tabAktif by rememberSaveable { mutableStateOf("Tersedia") }
    val tabList = listOf("Tersedia", "Berjalan", "Selesai")

    var listPesananDb by remember { mutableStateOf<List<PesananModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (isPreview) {
            listPesananDb = listOf(
                PesananModel("TRX-260882", "raflisgk@gmail.com", "Titip Beli (kue)", "Hari ini, Segera", "Mencari Helper", "Rp 31.000"),
                PesananModel("TRX-441122", "raflisgk@gmail.com", "Titip Barang (Paket Kecil)", "Sekarang", "Mencari Helper", "Rp 13.000")
            )
            isLoading = false
        } else {
            val sharedPref = context.getSharedPreferences("TolonginPref", android.content.Context.MODE_PRIVATE)
            val emailLogin = sharedPref.getString("USER_EMAIL", "") ?: ""
            val roleLogin  = sharedPref.getString("USER_ROLE", "user") ?: "user"

            if (emailLogin.isEmpty()) {
                isLoading = false
                return@LaunchedEffect
            }

            val emailQuery = if (roleLogin == "helper") "semua" else emailLogin

            RetrofitClient.instance.getPesanan(emailQuery).enqueue(object : Callback<List<PesananModel>> {
                override fun onResponse(call: Call<List<PesananModel>>, response: Response<List<PesananModel>>) {
                    isLoading = false
                    if (response.isSuccessful && response.body() != null) {
                        listPesananDb = response.body()!!
                    }
                }
                override fun onFailure(call: Call<List<PesananModel>>, t: Throwable) {
                    isLoading = false
                    Toast.makeText(context, "Gagal terhubung: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    val mitraPrefs = context.getSharedPreferences("MitraPrefs", android.content.Context.MODE_PRIVATE)
    val isCleaning = mitraPrefs.getBoolean("isCleaning", true)
    val isAntar    = mitraPrefs.getBoolean("isAntar", true)
    val isTitip    = mitraPrefs.getBoolean("isTitip", false)
    val emailHelper = context.getSharedPreferences("TolonginPref", android.content.Context.MODE_PRIVATE)
        .getString("USER_EMAIL", "") ?: ""

    val pesananTerfilter = listPesananDb.filter { item ->
        android.util.Log.d("DEBUG_FILTER", "id: ${item.id_transaksi} | status: ${item.status} | email_helper: '${item.email_helper}' | emailHelper: '$emailHelper'")
        val layananDiizinkan = when {
            item.nama_layanan.contains("Bersih", ignoreCase = true) ||
                    item.nama_layanan.contains("Clean", ignoreCase = true) ||
                    item.nama_layanan.contains("Kos", ignoreCase = true) ||
                    item.nama_layanan.contains("Kamar", ignoreCase = true) -> isCleaning

            item.nama_layanan.contains("Antar", ignoreCase = true) -> isAntar

            item.nama_layanan.contains("Titip", ignoreCase = true) ||
                    item.nama_layanan.contains("Beli", ignoreCase = true)  -> isTitip

            else -> false
        }

        val filterTab = when (tabAktif) {
            "Tersedia" -> item.status.contains("Mencari", ignoreCase = true) ||
                    item.status.contains("Lunas", ignoreCase = true)
            "Berjalan" -> {
                android.util.Log.d("DEBUG_EMAIL_HELPER", "db: '${item.email_helper}' | local: '$emailHelper' | sama: ${item.email_helper == emailHelper}")
                item.status.contains("Progres", ignoreCase = true) ||
                        item.status.contains("Jalan", ignoreCase = true)
            }
            "Selesai"  -> item.status.contains("Selesai", ignoreCase = true)
            else       -> true
        }

        layananDiizinkan && filterTab
    }

    Scaffold(
        containerColor = DBgPage,
        bottomBar = {
            CustomBottomNavMitra(navController = navController, tabAktif = "Pesanan")
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
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
                        modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(DPrimary)
                    ) {
                        Text("T", color = Color.White, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.ExtraBold))
                    }
                    Text("Tolong.in", color = DPrimary, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(36.dp).clip(CircleShape).background(DBgCard)
                    ) {
                        Text("🔔", style = TextStyle(fontSize = 16.sp))
                    }
                }
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(top = 4.dp, bottom = 20.dp)
                ) {
                    Text("Pesanan Baru", color = DTextDark, style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.ExtraBold))
                    Text("Temukan tugas di sekitarmu", color = DTextMid, style = TextStyle(fontSize = 14.sp))
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 24.dp)
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
                                style = TextStyle(fontSize = 14.sp, fontWeight = if (aktif) FontWeight.SemiBold else FontWeight.Normal)
                            )
                        }
                    }
                }
            }

            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = DPrimary)
                    }
                }
            } else if (pesananTerfilter.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                        Text("Tidak ada pesanan di tab ini.", color = DTextMid, fontSize = 14.sp)
                    }
                }
            } else {
                items(pesananTerfilter.size) { index ->
                    val item = pesananTerfilter[index]
                    PesananCardItem(
                        item = item,
                        tabAktif = tabAktif,
                        onCardClick = {
                            val safeStatus = Uri.encode(item.status.ifEmpty { "Mencari" })
                            navController.navigate("detail_pesanan_mitra/${item.id_transaksi}/$safeStatus")
                        },
                        onAksiClick = {
                            if (tabAktif == "Tersedia") {
                                listPesananDb = listPesananDb.map { order ->
                                    if (order.id_transaksi == item.id_transaksi) order.copy(status = "Progres") else order
                                }
                                Toast.makeText(context, "Pesanan diambil! Masuk ke tab Berjalan.", Toast.LENGTH_SHORT).show()

                                val sharedPref = context.getSharedPreferences("TolonginPref", android.content.Context.MODE_PRIVATE)
                                val emailHelperLogin = sharedPref.getString("USER_EMAIL", "") ?: ""

                                android.util.Log.d("DEBUG_AMBIL", "Memanggil ambilPesanan: ${item.id_transaksi} - $emailHelperLogin")

                                RetrofitClient.instance.ambilPesanan(
                                    item.id_transaksi, emailHelperLogin
                                ).enqueue(object : Callback<ResponseModel> {
                                    override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                                        val msg = response.body()?.message ?: "null"
                                        val code = response.code()
                                        val raw = response.errorBody()?.string() ?: "kosong"
                                        android.util.Log.d("DEBUG_RESPONSE", "code: $code | msg: $msg | error: $raw")
                                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                                            Toast.makeText(context, "code:$code msg:$msg", Toast.LENGTH_LONG).show()
                                        }
                                        RetrofitClient.instance.getPesanan("semua").enqueue(object : Callback<List<PesananModel>> {
                                            override fun onResponse(call: Call<List<PesananModel>>, response: Response<List<PesananModel>>) {
                                                if (response.isSuccessful && response.body() != null) {
                                                    android.os.Handler(android.os.Looper.getMainLooper()).post {
                                                        listPesananDb = response.body()!!
                                                        android.util.Log.d("DEBUG_FILTER", "listPesananDb updated: ${listPesananDb.size} items")
                                                    }
                                                }
                                            }
                                            override fun onFailure(call: Call<List<PesananModel>>, t: Throwable) {
                                                android.os.Handler(android.os.Looper.getMainLooper()).post {
                                                    Toast.makeText(context, "Gagal refresh: ${t.message}", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        })
                                    }
                                    override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                                            Toast.makeText(context, "Gagal ambil: ${t.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                })

                            } else if (tabAktif == "Berjalan") {
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
    }
}

@Composable
fun PesananCardItem(
    item: PesananModel,
    tabAktif: String,
    onCardClick: () -> Unit,
    onAksiClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val kategoriTampil = when {
        item.nama_layanan.contains("Clean", ignoreCase = true) ||
                item.nama_layanan.contains("Bersih", ignoreCase = true) ||
                item.nama_layanan.contains("Kos", ignoreCase = true) ||
                item.nama_layanan.contains("Kamar", ignoreCase = true) -> "Kebersihan"
        item.nama_layanan.contains("Beli", ignoreCase = true) ||
                item.nama_layanan.contains("Titip", ignoreCase = true) -> "Kurir"
        else -> "Perbaikan"
    }

    val badgeIcon = when (kategoriTampil) {
        "Kebersihan" -> "🧹"
        "Kurir"      -> "🚚"
        else         -> "📦"
    }

    val deskripsiDummy = "Menerima layanan ${item.nama_layanan} terkonfirmasi resmi dari sistem aplikasi pelanggan Tolong.in."

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
        "Tersedia" -> DPrimaryDark
        "Berjalan" -> Color(0xFF10B981)
        else       -> Color(0xFF9CA3AF)
    }

    val tombolBisaDiklik = tabAktif != "Selesai"

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

@Preview(widthDp = 390, heightDp = 844)
@Composable
fun DaftarPesananPreview() {
    DaftarPesananScreen(navController = rememberNavController())
}