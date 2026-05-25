package com.example.tolongin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.text.NumberFormat
import java.util.Locale
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTitipBeliScreen(navController: NavController) { // NAMA FUNGSI SUDAH DIUBAH
    // --- STATE UNTUK MENYIMPAN INPUTAN ---
    var namaProduk by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("1") }
    var estimasiHargaInput by remember { mutableStateOf("") }
    var catatan by remember { mutableStateOf("") }
    var alamatTujuan by remember { mutableStateOf("") }

    // --- LOGIKA PERHITUNGAN BIAYA ---
    val hargaProduk = estimasiHargaInput.filter { it.isDigit() }.toIntOrNull() ?: 0
    val biayaLayanan = 1000
    val biayaPengiriman = 15000
    val totalBiaya = hargaProduk + biayaLayanan + biayaPengiriman

    // Fungsi format Rupiah
    fun formatRupiah(number: Int): String {
        val format = NumberFormat.getNumberInstance(Locale("id", "ID"))
        return "Rp ${format.format(number)}"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("", color = Color(0xFF1E2D5A), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color(0xFF1E2D5A))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 16.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                PaddingValues(horizontal = 24.dp, vertical = 16.dp)
                Button(
                    onClick = {
                        // Encode data agar tidak error jika pelanggan memasukkan spasi atau tanda baca
                        val safeNama = if(namaProduk.isEmpty()) "Titip Beli" else android.net.Uri.encode(namaProduk)
                        val safeCatatan = if(catatan.isEmpty()) "-" else android.net.Uri.encode(catatan)
                        val safeAlamat = if(alamatTujuan.isEmpty()) "-" else android.net.Uri.encode(alamatTujuan)

                        // Kirim data ke rute MainActivity
                        val route = "konfirmasi/${safeNama}/${safeCatatan}/${safeAlamat}/${hargaProduk}"
                        navController.navigate(route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Lanjut ke Konfirmasi", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Text(text = "Detail Pesanan", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Lengkapi detail pesanan Anda agar Helper kami dapat memberikan pelayanan terbaik.",
                fontSize = 14.sp,
                color = Color(0xFF4E5A81),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomTextFieldTitipBeli( // NAMA FUNGSI SUDAH DIUBAH
                label = "NAMA PRODUK / LINK TOKO",
                value = namaProduk,
                onValueChange = { namaProduk = it },
                placeholder = "Contoh: Kopi Susu Gula Aren..."
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    CustomTextFieldTitipBeli(
                        label = "JUMLAH",
                        value = jumlah,
                        onValueChange = { jumlah = it },
                        keyboardType = KeyboardType.Number
                    )
                }
                Box(modifier = Modifier.weight(1.5f)) {
                    CustomTextFieldTitipBeli(
                        label = "ESTIMASI HARGA",
                        value = estimasiHargaInput,
                        onValueChange = { input ->
                            estimasiHargaInput = input.filter { it.isDigit() }
                        },
                        placeholder = "Rp 0",
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            CustomTextFieldTitipBeli(
                label = "CATATAN",
                value = catatan,
                onValueChange = { catatan = it },
                placeholder = "Tulis instruksi khusus (misal: kurangi gula...)",
                singleLine = false,
                modifier = Modifier.height(100.dp)
            )

            CustomTextFieldTitipBeli(
                label = "ALAMAT TUJUAN",
                value = alamatTujuan,
                onValueChange = { alamatTujuan = it },
                placeholder = "Jl. Kemang Raya No. 10...",
                leadingIcon = Icons.Default.LocationOn
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "HELPER TERPILIH", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4E5A81), letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                modifier = Modifier.fillMaxWidth().clickable { },
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFEFF0FF))) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.align(Alignment.Center))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Budi Santoso", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E2D5A))
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(Color(0xFFEFF0FF)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                Text("TERDEKAT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("4.9", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
                            Text(" (120+ Selesai)", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color(0xFF2563EB))
                }
            }

            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.End) {
                Text(text = "Lihat helper lain", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB), modifier = Modifier.clickable { })
            }

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(text = "Estimasi Biaya", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
                    Spacer(modifier = Modifier.height(16.dp))

                    ReceiptRowTitipBeli("Harga Produk", formatRupiah(hargaProduk)) // NAMA FUNGSI SUDAH DIUBAH
                    Spacer(modifier = Modifier.height(12.dp))
                    ReceiptRowTitipBeli("Layanan Aplikasi", formatRupiah(biayaLayanan))
                    Spacer(modifier = Modifier.height(12.dp))
                    ReceiptRowTitipBeli("Biaya Pengiriman", formatRupiah(biayaPengiriman))

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF3F4F6), thickness = 1.dp)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Total", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2D5A))
                        Text(text = formatRupiah(totalBiaya), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF60A5FA), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Harga aktual dapat berubah sesuai struk pembelian.",
                            fontSize = 12.sp,
                            color = Color(0xFF4E5A81)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// --- FUNGSI TELAH DIGANTI NAMANYA AGAR TIDAK BENTROK ---
@Composable
private fun CustomTextFieldTitipBeli(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4E5A81),
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray) },
            leadingIcon = if (leadingIcon != null) { { Icon(leadingIcon, contentDescription = null, tint = Color(0xFF2563EB)) } } else null,
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFEFF0FF),
                unfocusedContainerColor = Color(0xFFEFF0FF),
                focusedBorderColor = Color(0xFF2563EB),
                unfocusedBorderColor = Color.Transparent
            ),
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}

@Composable
private fun ReceiptRowTitipBeli(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color(0xFF4E5A81), fontSize = 14.sp)
        Text(text = value, color = Color(0xFF1E2D5A), fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun FormTitipBeliPreview() {
    MaterialTheme {
        FormTitipBeliScreen(navController = rememberNavController())
    }
}