package com.example.tolongin

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KonfirmasiPesananScreen(
    navController: NavController,
    namaProduk: String = "Jasa Belanja Harian",
    catatan: String = "-",
    alamat: String = "-",
    hargaProduk: Int = 0
) {
    // 1. Ambil context untuk SharedPreferences
    val context = LocalContext.current

    // --- MENGHITUNG OTOMATIS BIAYA ---
    val biayaLayanan = 1000
    val biayaPengiriman = 15000
    val totalBiaya = hargaProduk + biayaLayanan + biayaPengiriman

    fun formatRupiah(number: Int): String {
        val format = NumberFormat.getNumberInstance(Locale("id", "ID"))
        return "Rp ${format.format(number)}"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color(0xFF2563EB))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF8F9FF))
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 16.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        lineHeight = 18.sp,
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Gray, fontSize = 11.sp)) {
                                append("Dengan menekan tombol di bawah, Anda menyetujui seluruh ")
                            }
                            withStyle(style = SpanStyle(color = Color(0xFF2563EB), fontSize = 11.sp, fontWeight = FontWeight.Bold)) {
                                append("Syarat & Ketentuan (S&K)")
                            }
                            withStyle(style = SpanStyle(color = Color.Gray, fontSize = 11.sp)) {
                                append("\nserta kebijakan privasi yang berlaku di Tolong.in.")
                            }
                        }
                    )
                    Button(
                        // ====================================================================
                        // LOGIKA ALUR BARU: TITIP DATA KE MEMORI LALU MASUK KE HALAMAN QRIS
                        // ====================================================================
                        onClick = {
                            val idTrxAcak = "TRX-${(100000..999999).random()}"
                            val hargaFormatted = formatRupiah(totalBiaya)

                            val sharedPreferences = context.getSharedPreferences("TolonginPref", Context.MODE_PRIVATE)
                            val emailLogin = sharedPreferences.getString("USER_EMAIL", "") ?: ""

                            val pesananLama = sharedPreferences.getString("LIST_PESANAN", "[]") ?: "[]"
                            val listJson = org.json.JSONArray(pesananLama)

                            val obj = org.json.JSONObject().apply {
                                put("id_transaksi", idTrxAcak)
                                put("email", emailLogin)
                                put("nama_layanan", "Titip Beli ($namaProduk)")
                                put("tanggal", "Hari ini, Segera")
                                put("status", "Lunas")
                                put("total_harga", hargaFormatted)
                                put("nama_produk", namaProduk)
                                put("catatan", catatan)
                                put("alamat_tujuan", alamat)
                            }
                            listJson.put(obj)

                            sharedPreferences.edit().apply {
                                putString("LIST_PESANAN", listJson.toString())
                                putString("HARGALAYANAN", hargaFormatted)
                                putString("NAMALAYANAN", "Titip Beli ($namaProduk)")
                                putString("TRX_ID", idTrxAcak)
                                putString("SELECTED_DATE", "Hari ini, Segera")
                                apply()
                            }
                            // Kirim ke database
                            val body = okhttp3.FormBody.Builder()
                                .add("id_transaksi", idTrxAcak)
                                .add("email", emailLogin)
                                .add("nama_layanan", "Titip Beli ($namaProduk)")
                                .add("tanggal", "Hari ini, Segera")
                                .add("status", "Lunas")
                                .add("total_harga", hargaFormatted)
                                .add("nama_produk", namaProduk)
                                .add("catatan", catatan)
                                .add("alamat_tujuan", alamat)
                                .add("jumlah", "1")
                                .build()

                            val request = okhttp3.Request.Builder()
                                .url("${RetrofitClient.BASE_URL}insert_titip_beli.php")
                                .post(body)
                                .build()

                            okhttp3.OkHttpClient().newCall(request).enqueue(object : okhttp3.Callback {
                                override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {}
                                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {}
                            })

                            navController.navigate("qris_payment")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005AB2)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Bayar Sekarang - ${formatRupiah(totalBiaya)}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        containerColor = Color(0xFFF8F9FF)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // --- HEADER CARD (BIRU) ---
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF2563EB), Color(0xFF005AB2))
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = namaProduk,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "\"Tolong belikan pesanan ini, pastikan sesuai dengan catatan.\"",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // --- RINGKASAN LAYANAN ---
            item {
                Column {
                    Text(
                        text = "RINGKASAN LAYANAN",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4E5A81),
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFF0F5FF).copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            InfoRowItem(Icons.Default.Inventory, "PRODUK", "Belanja & Antar")
                            InfoRowItem(Icons.Default.Notes, "CATATAN", catatan)
                            InfoRowItem(Icons.Default.LocationOn, "ALAMAT", alamat)

                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    InfoRowItem(Icons.Default.Schedule, "WAKTU", "Hari ini, Segera")
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    InfoRowItem(Icons.Default.Person, "HELPER", "Budi Santoso")
                                }
                            }
                        }
                    }
                }
            }

            // --- METODE PEMBAYARAN ---
            item {
                Column {
                    Text(
                        text = "METODE PEMBAYARAN",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4E5A81),
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFF0F5FF))
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF005AB2)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Outlined.CreditCard, contentDescription = null, tint = Color.White)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text("Tolong Wallet", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
                                        Text("Saldo: Rp 150.000", fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                                Text("Ganti", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB), modifier = Modifier.clickable { })
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            CostRow("Harga Produk", formatRupiah(hargaProduk))
                            Spacer(modifier = Modifier.height(8.dp))
                            CostRow("Layanan Aplikasi", formatRupiah(biayaLayanan))
                            Spacer(modifier = Modifier.height(8.dp))
                            CostRow("Biaya Pengiriman", formatRupiah(biayaPengiriman))

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total Pembayaran", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
                                Text(formatRupiah(totalBiaya), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                            }
                        }
                    }
                }
            }

            // --- PROTEKSI ---
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFF0F5FF).copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, Color(0xFFDBEAFE)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Tolong.in Protection", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
                        }
                        ProtectionItem(Icons.Default.CreditScore, "Garansi uang kembali", "Dana aman jika pesanan tidak sesuai kesepakatan.")
                        ProtectionItem(Icons.Default.HealthAndSafety, "Asuransi barang hilang/rusak", "Proteksi hingga Rp 1.000.000 untuk barang Anda.")
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun InfoRowItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, contentDescription = null, tint = Color(0xFF60A5FA), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4E5A81), letterSpacing = 0.5.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 13.sp, color = Color(0xFF1E2D5A), lineHeight = 18.sp)
        }
    }
}

@Composable
private fun CostRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, fontSize = 14.sp, color = Color(0xFF4E5A81))
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E2D5A))
    }
}

@Composable
private fun ProtectionItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, desc: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, contentDescription = null, tint = Color(0xFF60A5FA), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = desc, fontSize = 11.sp, color = Color(0xFF4E5A81), lineHeight = 16.sp)
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun KonfirmasiPesananPreview() {
    MaterialTheme {
        KonfirmasiPesananScreen(navController = rememberNavController())
    }
}