package com.example.tolongin.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tolongin.PesananModel
import com.example.tolongin.PrimaryBlue
import com.example.tolongin.RetrofitClient
import com.example.tolongin.viewmodel.PesananViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarPesananScreen(
    navController: NavController,
    viewModel: PesananViewModel
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    var listPesananDb by remember { mutableStateOf<List<PesananModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf(0) }
    var selectedPesanan by remember { mutableStateOf<PesananModel?>(null) }
    val sheetState = rememberModalBottomSheetState()

    val bgPage = Color(0xFFF8FAFC)
    val textDark = Color(0xFF212D51)
    val textMid = Color(0xFF4E5A81)

    LaunchedEffect(Unit) {
        if (isPreview) {
            listPesananDb = listOf(
                PesananModel("TRX-260882", "raflisgk@gmail.com", "Titip Beli (Kopi Susu)", "Hari ini, Segera", "Lunas", "Rp 16.000"),
                PesananModel("TRX-886929", "raflisgk@gmail.com", "Titip Barang (Paket Kecil)", "Sekarang (Langsung Jemput)", "Lunas", "Rp 18.000"),
                PesananModel("TRX-933710", "raflisgk@gmail.com", "Titip Barang (Barang Besar)", "Sekarang (Langsung Jemput)", "Lunas", "Rp 18.000")
            )
            isLoading = false
        } else {
            val sharedPref = context.getSharedPreferences("TolonginPref", android.content.Context.MODE_PRIVATE)
            val emailLogin = sharedPref.getString("USER_EMAIL", "") ?: ""

            if (emailLogin.isEmpty()) {
                isLoading = false
                return@LaunchedEffect
            }

            RetrofitClient.instance.getPesanan(emailLogin).enqueue(object : Callback<List<PesananModel>> {
                override fun onResponse(call: Call<List<PesananModel>>, response: Response<List<PesananModel>>) {
                    isLoading = false
                    if (response.isSuccessful && response.body() != null) {
                        listPesananDb = response.body()!!
                    }
                }
                override fun onFailure(call: Call<List<PesananModel>>, t: Throwable) {
                    isLoading = false
                    Toast.makeText(context, "Gagal memuat database: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    val listMendatang = listPesananDb.filter {
        !it.status.contains("Selesai", ignoreCase = true)
    }
    val listRiwayat = listPesananDb.filter {
        it.status.contains("Selesai", ignoreCase = true)
    }

    // ── BOTTOM SHEET DETAIL ──
    if (selectedPesanan != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedPesanan = null },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            val pesanan = selectedPesanan!!
            val isTitipBeli = pesanan.nama_layanan.contains("Titip", ignoreCase = true) ||
                    pesanan.nama_layanan.contains("Beli", ignoreCase = true)
            val isKirimBarang = pesanan.nama_layanan.contains("Barang", ignoreCase = true) ||
                    pesanan.nama_layanan.contains("Paket", ignoreCase = true) ||
                    pesanan.nama_layanan.contains("Kirim", ignoreCase = true)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp).height(4.dp)
                        .clip(RoundedCornerShape(9999.dp))
                        .background(Color(0xFFE5E7EB))
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryBlue.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = when {
                                isTitipBeli -> "🛍️"
                                isKirimBarang -> "📦"
                                else -> "🧹"
                            },
                            fontSize = 22.sp
                        )
                    }
                    Column {
                        Text(
                            text = when {
                                isTitipBeli -> "Detail Titip Beli"
                                isKirimBarang -> "Detail Kirim Barang"
                                else -> "Detail Pesanan"
                            },
                            fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF212D51)
                        )
                        Text(pesanan.id_transaksi, fontSize = 11.sp, color = Color(0xFF6B7280))
                    }
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(Modifier.height(16.dp))

                Text("INFO LAYANAN", fontSize = 10.sp, fontWeight = FontWeight.Bold,
                    color = Color(0xFF9CA3AF), letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(bottom = 12.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Layanan", fontSize = 13.sp, color = Color(0xFF6B7280))
                    Text(pesanan.nama_layanan, fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF212D51), maxLines = 2, overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = 200.dp))
                }
                Spacer(Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Jadwal", fontSize = 13.sp, color = Color(0xFF6B7280))
                    Text(pesanan.tanggal, fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF212D51), maxLines = 2, overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = 200.dp))
                }
                Spacer(Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Status", fontSize = 13.sp, color = Color(0xFF6B7280))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                when {
                                    pesanan.status.contains("Selesai", ignoreCase = true) -> Color(0xFFD1FAE5)
                                    pesanan.status.contains("Progres", ignoreCase = true) -> Color(0xFFFEF3C7)
                                    pesanan.status.contains("Lunas", ignoreCase = true) -> Color(0xFFEFF6FF)
                                    else -> Color(0xFFF3F4F6)
                                }
                            )
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = pesanan.status,
                            fontSize = 12.sp, fontWeight = FontWeight.Bold,
                            color = when {
                                pesanan.status.contains("Selesai", ignoreCase = true) -> Color(0xFF059669)
                                pesanan.status.contains("Progres", ignoreCase = true) -> Color(0xFFD97706)
                                pesanan.status.contains("Lunas", ignoreCase = true) -> PrimaryBlue
                                else -> Color(0xFF6B7280)
                            }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(Modifier.height(16.dp))

                // ── SECTION KHUSUS TITIP BELI ──
                if (isTitipBeli) {
                    Text("DETAIL TITIP BELI", fontSize = 10.sp, fontWeight = FontWeight.Bold,
                        color = Color(0xFF9CA3AF), letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(bottom = 12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8FAFC))
                            .padding(12.dp)
                    ) {
                        Text("🛒", fontSize = 16.sp)
                        Column {
                            Text("Nama Produk", fontSize = 11.sp, color = Color(0xFF9CA3AF))
                            Text(pesanan.nama_layanan, fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold, color = Color(0xFF212D51))
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8FAFC))
                            .padding(12.dp)
                    ) {
                        Text("📍", fontSize = 16.sp)
                        Column {
                            Text("Alamat Tujuan", fontSize = 11.sp, color = Color(0xFF9CA3AF))
                            Text("Jl. Kemang Raya No. 10", fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold, color = Color(0xFF212D51))
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(Modifier.height(16.dp))
                }

                // ── SECTION KHUSUS KIRIM BARANG ──
                if (isKirimBarang) {
                    Text("DETAIL PENGIRIMAN", fontSize = 10.sp, fontWeight = FontWeight.Bold,
                        color = Color(0xFF9CA3AF), letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(bottom = 12.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8FAFC))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("🟢", fontSize = 12.sp)
                            Column {
                                Text("LOKASI PENJEMPUTAN", fontSize = 10.sp,
                                    color = Color(0xFF9CA3AF), fontWeight = FontWeight.Bold)
                                Text("Lokasi Pengguna", fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold, color = Color(0xFF212D51))
                            }
                        }
                        HorizontalDivider(color = Color(0xFFE5E7EB))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("🔴", fontSize = 12.sp)
                            Column {
                                Text("TUJUAN PENGIRIMAN", fontSize = 10.sp,
                                    color = Color(0xFF9CA3AF), fontWeight = FontWeight.Bold)
                                Text("Tujuan Pengiriman", fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold, color = Color(0xFF212D51))
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    val jenisPaket = when {
                        pesanan.nama_layanan.contains("Kecil", ignoreCase = true) -> "📦 Paket Kecil (Maks 5 kg)"
                        pesanan.nama_layanan.contains("Sedang", ignoreCase = true) -> "📦 Paket Sedang (5-15 kg)"
                        pesanan.nama_layanan.contains("Besar", ignoreCase = true) -> "🚛 Barang Besar (15-50 kg)"
                        pesanan.nama_layanan.contains("Fragile", ignoreCase = true) -> "⚠️ Fragile (Perlu Bubble Wrap)"
                        else -> "📦 Paket Standar"
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEFF6FF))
                            .padding(12.dp)
                    ) {
                        Text("JENIS PAKET", fontSize = 11.sp, color = Color(0xFF6B7280))
                        Text(jenisPaket, fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold, color = PrimaryBlue)
                    }

                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(Modifier.height(16.dp))
                }

                // ── RINGKASAN BIAYA ──
                Text("RINGKASAN BIAYA", fontSize = 10.sp, fontWeight = FontWeight.Bold,
                    color = Color(0xFF9CA3AF), letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(bottom = 12.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF0F5FF))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isTitipBeli) {
                        BiayaRowItem("Harga Produk", "Sesuai struk")
                        BiayaRowItem("Layanan Aplikasi", "Rp 1.000")
                        BiayaRowItem("Biaya Pengiriman", "Rp 15.000")
                    } else if (isKirimBarang) {
                        BiayaRowItem("Biaya Pengiriman", "Rp 10.000")
                        BiayaRowItem("Asuransi (Proteksi)", "Rp 2.000")
                        BiayaRowItem("Biaya Platform", "Rp 1.000")
                    } else {
                        BiayaRowItem("Biaya Layanan", pesanan.total_harga)
                    }
                    HorizontalDivider(color = Color(0xFFBFDBFE))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Total Pembayaran", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF212D51))
                        Text(pesanan.total_harga, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold,
                            color = PrimaryBlue)
                    }
                }

                Spacer(Modifier.height(20.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(PrimaryBlue)
                        .clickable { selectedPesanan = null }
                        .padding(vertical = 14.dp)
                ) {
                    Text("Tutup", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    Scaffold(
        containerColor = bgPage,
        bottomBar = { BottomNavigationBarOrders(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Pesanan Saya", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold,
                color = textDark, letterSpacing = (-0.5).sp)
            Text("Kelola layanan pilihan Anda", fontSize = 14.sp, color = textMid)
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth().height(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEEF2F6))
                    .padding(4.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedTab == 0) Color.White else Color.Transparent)
                        .clickable { selectedTab = 0 }
                ) {
                    Text("Mendatang", fontSize = 14.sp, fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 0) PrimaryBlue else textMid)
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedTab == 1) Color.White else Color.Transparent)
                        .clickable { selectedTab = 1 }
                ) {
                    Text("Riwayat", fontSize = 14.sp, fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 1) PrimaryBlue else textMid)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            } else {
                if (selectedTab == 0) {
                    if (listMendatang.isNotEmpty()) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(bottom = 24.dp)
                        ) {
                            items(listMendatang) { item ->
                                ActiveOrderCard(
                                    title = item.nama_layanan,
                                    status = item.status,
                                    date = item.tanggal,
                                    price = item.total_harga,
                                    orderId = item.id_transaksi,
                                    onClick = { selectedPesanan = item }
                                )
                            }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Belum ada pesanan aktif.", color = textMid, fontSize = 14.sp)
                        }
                    }
                } else {
                    if (listRiwayat.isNotEmpty()) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(bottom = 24.dp)
                        ) {
                            items(listRiwayat) { item ->
                                ActiveOrderCard(
                                    title = item.nama_layanan,
                                    status = item.status,
                                    date = item.tanggal,
                                    price = item.total_harga,
                                    orderId = item.id_transaksi,
                                    onClick = { selectedPesanan = item }
                                )
                            }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Belum ada riwayat transaksi.", color = textMid, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BiayaRowItem(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label, fontSize = 13.sp, color = Color(0xFF4E5A81))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF212D51))
    }
}

@Composable
fun ActiveOrderCard(
    title: String,
    status: String,
    date: String,
    price: String,
    orderId: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212D51))
                Text(text = orderId, fontSize = 11.sp, color = Color(0xFF6B7280), fontWeight = FontWeight.Medium)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(PrimaryBlue.copy(alpha = 0.1f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(text = status, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = Color(0xFFF1F5F9))
        Spacer(modifier = Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(text = "📅", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = date, fontSize = 13.sp, color = Color(0xFF4E5A81),
                maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(text = "TOTAL PEMBAYARAN", fontSize = 9.sp, color = Color(0xFF6B7280),
                    fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                Text(text = price, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = PrimaryBlue)
            }
            Text(text = "Lihat Detail ›", fontSize = 12.sp, color = PrimaryBlue, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun BottomNavigationBarOrders(navController: NavController, modifier: Modifier = Modifier) {
    val navItems = listOf(
        Icons.Filled.Home to "HOME",
        Icons.Outlined.ListAlt to "ORDERS",
        Icons.Outlined.ChatBubbleOutline to "MESSAGES",
        Icons.Outlined.Person to "PROFILE"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 24.dp,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { (icon, label) ->
                val active = label == "ORDERS"
                val bgColor = if (active) Color(0xFFF0F5FF) else Color.Transparent
                val contentColor = if (active) PrimaryBlue else Color(0xFF94A3B8)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(bgColor)
                        .clickable {
                            if (active) return@clickable
                            when (label) {
                                "HOME"     -> navController.navigate("beranda") { popUpTo(0); launchSingleTop = true }
                                "ORDERS"   -> navController.navigate("daftar_pesanan") { popUpTo(0); launchSingleTop = true }
                                "MESSAGES" -> navController.navigate("pesan") { popUpTo(0); launchSingleTop = true }
                                "PROFILE"  -> navController.navigate("profil") { popUpTo(0); launchSingleTop = true }
                            }
                        }
                        .padding(horizontal = 22.dp, vertical = 12.dp)
                ) {
                    Icon(imageVector = icon, contentDescription = label, tint = contentColor, modifier = Modifier.size(26.dp))
                    Spacer(Modifier.height(4.dp))
                    Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = contentColor)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun DaftarPesananScreenPreview() {
    DaftarPesananScreen(
        navController = rememberNavController(),
        viewModel = PesananViewModel()
    )
}