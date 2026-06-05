package com.example.tolongin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormPemesananScreen(
    navController: NavController,
    namaLayanan: String,
    hargaLayanan: String
) {
    val context = LocalContext.current

    var selectedDateText by remember { mutableStateOf("Belum Pilih Tanggal") }
    var selectedTime by remember { mutableStateOf("Siang") }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val dateMillis = datePickerState.selectedDateMillis
                    if (dateMillis != null) {
                        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                        selectedDateText = formatter.format(Date(dateMillis))
                    }
                    showDatePicker = false
                }) {
                    Text("PILIH", color = Color(0xff005ab2), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("BATAL") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 120.dp)
        ) {
            // --- TOP BAR ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color(0xff005ab2))
                }
                Text("Tolong.in", color = Color(0xff005ab2), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            // --- STEP INDICATOR ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StepItem("Layanan", Color(0xff005ab2), true, Icons.Default.CleaningServices)
                Box(Modifier.width(40.dp).height(2.dp).background(Color(0xff005ab2)))
                StepItem("Waktu & Lokasi", Color(0xff005ab2), true, null, "2")
                Box(Modifier.width(40.dp).height(2.dp).background(Color(0xffdbe1ff)))
                StepItem("Konfirmasi", Color(0xff212d51), false, null, "3")
            }

            Spacer(Modifier.height(32.dp))

            // --- PILIH JADWAL ---
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text("Pilih Jadwal Pembersihan", fontWeight = FontWeight.Bold, color = Color(0xff212d51))
                Spacer(Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth().shadow(12.dp, RoundedCornerShape(20.dp)).clickable { showDatePicker = true },
                    shape = RoundedCornerShape(20.dp),
                    color = if (selectedDateText == "Belum Pilih Tanggal") Color(0xfff8f9ff) else Color(0xff005ab2)
                ) {
                    Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarMonth, null, tint = if (selectedDateText == "Belum Pilih Tanggal") Color(0xff005ab2) else Color.White)
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text("Tanggal Layanan", fontSize = 12.sp, color = if (selectedDateText == "Belum Pilih Tanggal") Color.Gray else Color.White.copy(0.7f))
                            Text(text = selectedDateText, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = if (selectedDateText == "Belum Pilih Tanggal") Color.Black else Color.White)
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // --- SLOT WAKTU ---
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text("Pilih Slot Waktu", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TimeCard("Pagi", "08:00 - 11:00", selectedTime == "Pagi", Modifier.weight(1f)) { selectedTime = "Pagi" }
                    TimeCard("Siang", "12:00 - 15:00", selectedTime == "Siang", Modifier.weight(1f)) { selectedTime = "Siang" }
                    TimeCard("Sore", "16:00 - 19:00", selectedTime == "Sore", Modifier.weight(1f)) { selectedTime = "Sore" }
                }
            }

            Spacer(Modifier.height(32.dp))

            // --- ALAMAT ---
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Alamat Pembersihan", fontWeight = FontWeight.Bold)
                    Text("+ Tambah Baru", color = Color(0xff005ab2), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(12.dp))
                Box(Modifier.fillMaxWidth().border(2.dp, Color(0xff005ab2), RoundedCornerShape(24.dp)).padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(Modifier.size(40.dp), color = Color(0xff64a1ff), shape = RoundedCornerShape(12.dp)) {
                            Icon(Icons.Default.HomeWork, null, tint = Color.White, modifier = Modifier.padding(8.dp))
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text("Rumah Utama (Alex)", fontWeight = FontWeight.Bold)
                            Text("Jalan Melati No. 42, Kebayoran Baru", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }

        // --- BOTTOM BAR ---
        Surface(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            color = Color(0xfff6f5ff).copy(alpha = 0.95f),
            shadowElevation = 20.dp
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("ESTIMASI BIAYA", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text(hargaLayanan, fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color(0xff005ab2))
                }
                Button(
                    onClick = {
                        // ====================================================================
                        // FIX: SINKRONISASI CACHE PENUH UNTUK TRACKING FITUR BERSIH-BERSIH
                        // ====================================================================
                        val sharedPreferences = context.getSharedPreferences("TolonginPref", android.content.Context.MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            // Data internal lama Anda
                            putString("SELECTED_DATE", selectedDateText)
                            putString("NAMALAYANAN", namaLayanan)
                            putString("HARGALAYANAN", hargaLayanan)

                            // Data Baru Sinkronisasi ke TrackingPesananScreen
                            putString("PREF_CLEAN_LAYANAN", namaLayanan)
                            putString("PREF_CLEAN_ALAMAT", "Jalan Melati No. 42, Kebayoran Baru")
                            putString("PREF_CLEAN_WAKTU", "$selectedDateText | $selectedTime")
                            putString("PREF_CLEAN_CATATAN", "Pembersihan Area Rumah Utama")
                            apply()
                        }

                        navController.navigate(
                            "pembayaran/" +
                                    "$namaLayanan/" +
                                    "$hargaLayanan/" +
                                    "$selectedDateText/" +
                                    "Rumah Utama (Alex)/" +
                                    "$selectedTime"
                        )
                    },
                    enabled = selectedDateText != "Belum Pilih Tanggal",
                    modifier = Modifier.height(52.dp).width(160.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff005ab2))
                ) {
                    Text("Lanjutkan", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StepItem(label: String, color: Color, isActive: Boolean, icon: ImageVector? = null, num: String = "") {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(36.dp).clip(RoundedCornerShape(99.dp)).background(if (isActive) Color(0xff005ab2) else Color(0xffdbe1ff)), contentAlignment = Alignment.Center) {
            if (icon != null) Icon(icon, null, tint = Color.White, modifier = Modifier.size(18.dp))
            else Text(num, color = if (isActive) Color.White else Color(0xff212d51), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Text(label, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TimeCard(label: String, time: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier.height(100.dp).clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0xff005ab2) else Color(0xfff8f9ff))
            .border(1.dp, if (isSelected) Color.Transparent else Color(0xffdbe1ff), RoundedCornerShape(16.dp))
            .clickable { onClick() }.padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Schedule, null, tint = if (isSelected) Color.White else Color(0xff005ab2), modifier = Modifier.size(20.dp))
            Text(label, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else Color.Black, fontSize = 14.sp)
            Text(time, fontSize = 9.sp, color = if (isSelected) Color.White.copy(0.8f) else Color.Gray, textAlign = TextAlign.Center)
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 800)
@Composable
fun FormPemesananPreview() {
    MaterialTheme {
        FormPemesananScreen(
            navController = rememberNavController(),
            namaLayanan = "Pembersihan Rumah",
            hargaLayanan = "Rp 250.000"
        )
    }
}