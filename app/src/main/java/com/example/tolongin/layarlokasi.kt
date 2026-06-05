package com.example.tolongin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayarLokasi(
    onAllowClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    // ── LOGIKA INTEGRASI GPS ASLI ─────────────────────────────────────
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var isLoading by remember { mutableStateOf(false) }

    // Launcher untuk menghandle pop-up izin GPS bawaan Android
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineGranted || coarseGranted) {
            // Jika user klik 'Allow', lacak jalan aslinya
            ambilGpsAlamatNyata(context, fusedLocationClient) {
                isLoading = false
                onAllowClick()
            }
        } else {
            isLoading = false
            Toast.makeText(context, "Izin ditolak. Menggunakan lokasi default.", Toast.LENGTH_SHORT).show()
            onAllowClick()
        }
    }
    // ──────────────────────────────────────────────────────────────────

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xfff6f5ff))
    ) {
        // --- Efek Background Blur ---
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-50).dp)
                .size(300.dp)
                .blur(100.dp)
                .background(Color(0xff64a1ff).copy(alpha = 0.15f), CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // --- Ilustrasi Kartu Lokasi ---
            Box(
                modifier = Modifier.size(280.dp),
                contentAlignment = Alignment.Center
            ) {
                // Kartu belakang (biru muda)
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .rotate(-6f)
                        .clip(RoundedCornerShape(48.dp))
                        .background(Color(0xffeff0ff))
                )
                // Kartu depan (Putih)
                Surface(
                    modifier = Modifier
                        .size(240.dp)
                        .rotate(3f)
                        .shadow(15.dp, RoundedCornerShape(48.dp)),
                    shape = RoundedCornerShape(48.dp),
                    color = Color.White
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_sparkle),
                            contentDescription = "Location Icon",
                            colorFilter = ColorFilter.tint(Color(0xff005ab2)),
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // --- Teks Konten ---
            Text(
                text = "PRISTINE CURATOR",
                color = Color(0xff005ab2),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Aktifkan Lokasi",
                color = Color(0xff212d51),
                style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Kami membutuhkan lokasi Anda untuk menemukan penyedia layanan terbaik di dekat Anda",
                color = Color(0xff4e5a81),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // --- Info Privasi ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xffeff0ff).copy(alpha = 0.5f))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_sparkle),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color(0xff005ab2)),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = "Privasi Utama", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = "Lokasi Anda tidak akan dibagikan.", fontSize = 12.sp, color = Color(0xff4e5a81))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Tombol Akses Lokasi Otomatis ---
            Button(
                onClick = {
                    isLoading = true
                    val fineCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    val coarseCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

                    if (fineCheck || coarseCheck) {
                        // Jika dari awal sudah diizinkan, langsung tembak ambil alamat nyata
                        ambilGpsAlamatNyata(context, fusedLocationClient) {
                            isLoading = false
                            onAllowClick()
                        }
                    } else {
                        // Jika belum diizinkan, panggil launcher pop-up izin Android
                        permissionLauncher.launch(
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xff005ab2)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text("Izinkan Akses", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Text(
                text = "LEWATI",
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .clickable { onSkipClick() },
                style = TextStyle(
                    color = Color(0xff4e5a81),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ── FUNGSI STANDAR: MERUBAH KOORDINAT MENJADI ALAMAT NYATA JALANAN ──
private fun ambilGpsAlamatNyata(
    context: Context,
    fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient,
    onComplete: () -> Unit
) {
    try {
        fusedLocationClient.lastLocation.addOnSuccessListener { lokasi ->
            if (lokasi != null) {
                val geocoder = Geocoder(context, Locale.getDefault())
                // Ambil 1 baris hasil alamat terdekat dari koordinat GPS
                val hasilAlamat = geocoder.getFromLocation(lokasi.latitude, lokasi.longitude, 1)

                if (!hasilAlamat.isNullOrEmpty()) {
                    val alamatLengkapJalan = hasilAlamat[0].getAddressLine(0)

                    // Simpan alamat asli ke SharedPreferences agar bisa diambil oleh file mana pun
                    val sharedPref = context.getSharedPreferences("TolonginPref", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("ALAMAT_GPS_ASLI", alamatLengkapJalan).apply()
                }
            }
            onComplete()
        }.addOnFailureListener {
            onComplete()
        }
    } catch (e: SecurityException) {
        onComplete()
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun LayarLokasiPreview() {
    LayarLokasi(onAllowClick = {}, onSkipClick = {})
}