package com.example.tolongin.screens

import android.content.Context
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
import androidx.compose.ui.draw.shadow
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
            val emailLogin = "raflisgk@gmail.com"
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

    Box(modifier = Modifier.fillMaxSize().background(bgPage)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, start = 24.dp, end = 24.dp, bottom = 90.dp)
        ) {
            Text(
                text = "Pesanan Saya",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textDark,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = "Kelola layanan pilihan Anda",
                fontSize = 14.sp,
                color = textMid
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Toggle Tab
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEEF2F6))
                    .padding(4.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedTab == 0) Color.White else Color.Transparent)
                        .clickable { selectedTab = 0 }
                ) {
                    Text("Mendatang", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (selectedTab == 0) PrimaryBlue else textMid)
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedTab == 1) Color.White else Color.Transparent)
                        .clickable { selectedTab = 1 }
                ) {
                    Text("Riwayat", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (selectedTab == 1) PrimaryBlue else textMid)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            } else {
                if (selectedTab == 0) {
                    if (listPesananDb.isNotEmpty()) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(listPesananDb) { item ->
                                ActiveOrderCard(
                                    title = item.nama_layanan,
                                    status = item.status,
                                    date = item.tanggal,
                                    price = item.total_harga,
                                    orderId = item.id_transaksi,
                                    onClick = {
                                        // ─── LOGIKA NAVIGASI CLIK DATA DATABASE ───
                                        if (item.nama_layanan.contains("Barang") || item.nama_layanan.contains("Paket")) {
                                            // Ke Tracking Logistik (MVVM)
                                            navController.navigate("tracking_mvvm")
                                        } else if (item.nama_layanan.contains("Titip Beli")) {
                                            // Trik Pilihan ke-2: Kunci nama layanan ke cache HP sebelum pindah screen
                                            val sharedPref = context.getSharedPreferences("TolonginPref", Context.MODE_PRIVATE)
                                            with(sharedPref.edit()) {
                                                putString("PREF_NAMA_PRODUK", item.nama_layanan)
                                                putString("PREF_CATATAN", "Pembelian Terkonfirmasi")
                                                putString("PREF_ALAMAT_TUJUAN", "Jl. Raya Rungkut No. 4, Surabaya")
                                                apply()
                                            }

                                            // Ambil angka total harga saja untuk dioper ke rute bawaan Anda
                                            val hargaAngka = item.total_harga.filter { it.isDigit() }.toIntOrNull() ?: 70000
                                            val safeNama = android.net.Uri.encode(item.nama_layanan)

                                            // Pindah ke layar TrackingPesananScreen Anda
                                            navController.navigate("tracking/$safeNama/$hargaAngka")
                                        }
                                    }
                                )
                            }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Belum ada pesanan aktif.", color = textMid, fontSize = 14.sp)
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Belum ada riwayat transaksi.", color = textMid, fontSize = 14.sp)
                    }
                }
            }
        }

        BottomNavigationBarOrders(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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
            Text(
                text = date,
                fontSize = 13.sp,
                color = Color(0xFF4E5A81),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(text = "TOTAL PEMBAYARAN", fontSize = 9.sp, color = Color(0xFF6B7280), fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
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
                                "HOME"     -> navController.navigate("beranda") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                                "ORDERS"   -> navController.navigate("daftar_pesanan") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                                "MESSAGES" -> navController.navigate("pesan") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                                "PROFILE"  -> navController.navigate("profil") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
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