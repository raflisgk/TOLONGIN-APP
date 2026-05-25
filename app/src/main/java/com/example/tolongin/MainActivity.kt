package com.example.tolongin

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tolongin.screens.DetailPengirimanScreen
import com.example.tolongin.screens.KonfirmasiScreen
import com.example.tolongin.screens.TrackingScreen
import com.example.tolongin.viewmodel.PesananViewModel
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Deklarasi Navigasi & ViewModel cukup 1 kali di sini
                    val navController = rememberNavController()
                    val pesananViewModel: PesananViewModel = viewModel()

                    // ==========================================
                    // SATU NAVHOST UTAMA UNTUK SEMUA LAYAR
                    // ==========================================
                    NavHost(navController = navController, startDestination = "splash") {

                        composable("splash") {
                            LaunchedEffect(Unit) {
                                delay(2000)
                                navController.navigate("onboarding1") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }

                            HtmlBody()
                        }

                        composable("onboarding1") {
                            OnboardingPagerScreen(navController)
                        }

                        // --- 1. RUTE LAMA MAS ---
                        composable("login") { LoginScreen(navController) }
                        composable("beranda") { HomeScreen(navController) }
                        composable("detail_pembersihan") { PembersihanScreen(navController) }
                        composable(
                            "pemesanan/{namaLayanan}/{hargaLayanan}"
                        ) { backStackEntry ->

                            val namaLayanan =
                                backStackEntry.arguments?.getString("namaLayanan") ?: ""

                            val hargaLayanan =
                                backStackEntry.arguments?.getString("hargaLayanan") ?: ""

                            FormPemesananScreen(
                                navController = navController,
                                namaLayanan = namaLayanan,
                                hargaLayanan = hargaLayanan
                            )
                        }
                        composable("pemesanan_titip_beli") { FormTitipBeliScreen(navController = navController) }

                        composable(
                            "pembayaran/{namaLayanan}/{hargaLayanan}/{tanggal}/{alamat}/{waktu}"
                        ) { backStackEntry ->

                            val namaLayanan =
                                backStackEntry.arguments?.getString("namaLayanan") ?: ""

                            val hargaLayanan =
                                backStackEntry.arguments?.getString("hargaLayanan") ?: ""

                            val tanggal =
                                backStackEntry.arguments?.getString("tanggal") ?: ""

                            val alamat =
                                backStackEntry.arguments?.getString("alamat") ?: ""

                            val waktu =
                                backStackEntry.arguments?.getString("waktu") ?: ""

                            PembayaranScreen(
                                navController,
                                namaLayanan,
                                hargaLayanan,
                                tanggal,
                                alamat,
                                waktu
                            )
                        }

                        composable("qris/{idTransaksi}/{totalHarga}") { backStackEntry ->
                            val idTransaksi = backStackEntry.arguments?.getString("idTransaksi") ?: ""
                            val totalHarga = backStackEntry.arguments?.getString("totalHarga") ?: ""
                            QrisPaymentScreen(navController, "Cleaning Plus", totalHarga, idTransaksi)
                        }

                        composable("daftar_pesanan") { PesananScreen(navController) }
                        composable("google") { PilihAkunScreen(navController) }
                        composable("daftar") { DaftarScreen(navController) }
                        composable("profil") { ProfileScreen(navController) }
                        composable("informasipribadi") { InformasiPribadiScreen(navController) }
                        composable("pesan") { PesanScreen(navController) }

                        composable("layarlokasi") {
                            LayarLokasi(
                                onAllowClick = {
                                    navController.navigate("beranda") {
                                        popUpTo("layarlokasi") { inclusive = true }
                                    }
                                },
                                onSkipClick = {
                                    navController.navigate("beranda") {
                                        popUpTo("layarlokasi") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("konfirmasi/{namaProduk}/{catatan}/{alamat}/{hargaProduk}") { backStackEntry ->
                            val namaProduk = Uri.decode(backStackEntry.arguments?.getString("namaProduk") ?: "Titip Beli")
                            val catatan = Uri.decode(backStackEntry.arguments?.getString("catatan") ?: "-")
                            val alamat = Uri.decode(backStackEntry.arguments?.getString("alamat") ?: "-")
                            val hargaStr = backStackEntry.arguments?.getString("hargaProduk") ?: "0"

                            KonfirmasiPesananScreen(
                                navController = navController,
                                namaProduk = namaProduk,
                                catatan = catatan,
                                alamat = alamat,
                                hargaProduk = hargaStr.toIntOrNull() ?: 0
                            )
                        }

                        composable("tracking/{namaProduk}/{totalBiaya}") { backStackEntry ->
                            val namaProduk = Uri.decode(backStackEntry.arguments?.getString("namaProduk") ?: "Pesanan")
                            val biayaStr = backStackEntry.arguments?.getString("totalBiaya") ?: "0"

                            TrackingPesananScreen(
                                navController = navController,
                                namaProduk = namaProduk,
                                totalBiaya = biayaStr.toIntOrNull() ?: 0
                            ) // <-- TADI KURUNG INI YANG HILANG MAS
                        }


                        // --- 2. RUTE MVVM BARU KITA (Form -> Konfirmasi -> Tracking) ---
                        composable("detail_pengiriman") {
                            DetailPengirimanScreen(
                                viewModel = pesananViewModel,
                                onLanjut = { navController.navigate("konfirmasi_mvvm") }
                            )
                        }

                        composable("konfirmasi_mvvm") {
                            KonfirmasiScreen(
                                viewModel = pesananViewModel,
                                onBack = { navController.popBackStack() },
                                onBayar = { navController.navigate("tracking_mvvm") }
                            )
                        }

                        composable("tracking_mvvm") {
                            TrackingScreen(
                                viewModel = pesananViewModel,
                                onBack = {
                                    // Kembali ke beranda dan hapus tumpukan layar sebelumnya
                                    navController.navigate("beranda") {
                                        popUpTo("beranda") { inclusive = true }
                                    }
                                }
                            )
                        }

                    }
                }
            }
        }
    }
}