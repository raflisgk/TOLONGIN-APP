package com.example.tolongin.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tolongin.ResponseModel
import com.example.tolongin.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

// ── Warna UI ────────────────────────────────────────────────────────
private val DPrimary = Color(0xFF004A99)
private val DBg      = Color(0xFFF9FAFB)

@Composable
fun LaporanPenyelesaianScreen(
    idTransaksi: String,
    onBack: () -> Unit = {},
    navController: NavHostController // <--- Wajib ada untuk pindah ke beranda
) {
    var catatan by remember { mutableStateOf("") }
    var setuju by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // ── STATE UNTUK FOTO (Bitmap untuk Tampil, String Base64 untuk Dikirim) ──
    var bitmapSeb by remember { mutableStateOf<ImageBitmap?>(null) }
    var base64Seb by remember { mutableStateOf("") }

    var bitmapSes by remember { mutableStateOf<ImageBitmap?>(null) }
    var base64Ses by remember { mutableStateOf("") }

    // ── LAUNCHER GALERI FOTO SEBELUM ──
    val launcherSeb = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bitmap = getBitmapFromUri(context, it)
            bitmap?.let { btm ->
                bitmapSeb = btm.asImageBitmap() // Tampilkan di UI
                base64Seb = convertBitmapToBase64(btm) // Simpan ke Base64 untuk Database
            }
        }
    }

    // ── LAUNCHER GALERI FOTO SESUDAH ──
    val launcherSes = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bitmap = getBitmapFromUri(context, it)
            bitmap?.let { btm ->
                bitmapSes = btm.asImageBitmap() // Tampilkan di UI
                base64Ses = convertBitmapToBase64(btm) // Simpan ke Base64 untuk Database
            }
        }
    }

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
        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = DBg), modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFE0E7FF)), contentAlignment = Alignment.Center) {
                    Text("📋", fontSize = 20.sp)
                }
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text("Rincian Order", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    // ── TAMPILKAN ID ASLI DI LAYAR BIAR HELPER TAHU ──
                    Text("Order $idTransaksi - Penyelesaian Tugas", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Upload Bukti Foto", fontWeight = FontWeight.Bold)

        // ── AREA UPLOAD FOTO ──
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            UploadBox(
                label = "Foto Sebelum",
                subLabel = "Kondisi awal",
                bitmap = bitmapSeb,
                onClick = { launcherSeb.launch("image/*") },
                modifier = Modifier.weight(1f)
            )
            UploadBox(
                label = "Foto Sesudah",
                subLabel = "Hasil pengerjaan",
                bitmap = bitmapSes,
                onClick = { launcherSes.launch("image/*") },
                modifier = Modifier.weight(1f)
            )
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
            onClick = {
                // Pastikan foto sudah diisi sebelum kirim
                if (base64Seb.isEmpty() || base64Ses.isEmpty()) {
                    Toast.makeText(context, "Harap unggah Foto Sebelum & Sesudah!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val statusOrder = "Selesai"

                // Eksekusi Retrofit
                RetrofitClient.instance.kirimLaporanForm(
                    idTransaksi, statusOrder, base64Seb, base64Ses, catatan
                ).enqueue(object : Callback<ResponseModel> {
                    override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                        if (response.isSuccessful && response.body()?.status == "success") {
                            Toast.makeText(context, "Laporan Terkirim & Database Diperbarui!", Toast.LENGTH_SHORT).show()

                            // ── PERBAIKAN: Langsung lempar ke beranda dan hapus tumpukan layar ──
                            navController.navigate("beranda_helper") {
                                popUpTo(0) { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "Gagal: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                        Toast.makeText(context, "Koneksi ke server gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            enabled = setuju,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DPrimary)
        ) {
            Text("Kirim Laporan ➢", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

// ── FUNGSI KOMPONEN KOTAK FOTO ──
@Composable
fun UploadBox(label: String, subLabel: String, bitmap: ImageBitmap?, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        val stroke = Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .drawBehind { if (bitmap == null) drawRect(color = Color.Gray, style = stroke) }
                .background(DBg)
                .clickable { onClick() }, // Panggil galeri saat diklik
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                // Tampilkan gambar jika sudah dipilih
                Image(bitmap = bitmap, contentDescription = label, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            } else {
                // Tampilkan placeholder jika kosong
                Text("📷\nTambah Foto", textAlign = TextAlign.Center, color = Color.Gray, fontSize = 12.sp)
            }
        }
        Text(label, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        Text(subLabel, fontSize = 10.sp, color = Color.Gray)
    }
}

// ── FUNGSI PEMBANTU: KONVERSI URI KE BITMAP ──
fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    } catch (e: Exception) {
        null
    }
}

// ── FUNGSI PEMBANTU: KONVERSI BITMAP KE BASE64 (Dikompres agar tidak berat) ──
fun convertBitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    // Kompres foto 60% agar proses upload ke database lebih cepat & tidak error
    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}

// ── PERBAIKAN: FUNGSI PREVIEW ──
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun LaporanPenyelesaianPreview() {
    MaterialTheme {
        LaporanPenyelesaianScreen(
            idTransaksi = "TRX-DUMMY",
            navController = rememberNavController() // <--- Sudah disesuaikan
        )
    }
}