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
import com.example.tolongin.screens.DaftarPesananScreen
import com.example.tolongin.screens.BerandaMitraScreen
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
                    val navController = rememberNavController()
                    val pesananViewModel: PesananViewModel = viewModel()

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

                        composable("login") { LoginScreen(navController) }

                        // ── BERANDA USER BIASA ──
                        composable("beranda") { HomeScreen(navController) }

                        // ── BERANDA MITRA / HELPER ──
                        composable("beranda_helper") {
                            BerandaMitraScreen(navController = navController)
                        }

                        composable("detail_pembersihan") { PembersihanScreen(navController) }

                        composable("pemesanan/{namaLayanan}/{hargaLayanan}") { backStackEntry ->
                            val namaLayanan = backStackEntry.arguments?.getString("namaLayanan") ?: ""
                            val hargaLayanan = backStackEntry.arguments?.getString("hargaLayanan") ?: ""
                            FormPemesananScreen(navController, namaLayanan, hargaLayanan)
                        }

                        composable("pemesanan_titip_beli") { FormTitipBeliScreen(navController = navController) }

                        composable("pembayaran/{namaLayanan}/{hargaLayanan}/{tanggal}/{alamat}/{waktu}") { backStackEntry ->
                            val namaLayanan = backStackEntry.arguments?.getString("namaLayanan") ?: ""
                            val hargaLayanan = backStackEntry.arguments?.getString("hargaLayanan") ?: ""
                            val tanggal = backStackEntry.arguments?.getString("tanggal") ?: ""
                            val alamat = backStackEntry.arguments?.getString("alamat") ?: ""
                            val waktu = backStackEntry.arguments?.getString("waktu") ?: ""
                            PembayaranScreen(navController, namaLayanan, hargaLayanan, tanggal, alamat, waktu)
                        }

                        // Rute QRIS Lama (Jasa Pembersihan)
                        composable("qris/{idTransaksi}/{totalHarga}") { backStackEntry ->
                            val idTransaksi = backStackEntry.arguments?.getString("idTransaksi") ?: ""
                            val totalHarga = backStackEntry.arguments?.getString("totalHarga") ?: ""
                            QrisPaymentScreen(navController, "Cleaning Plus", totalHarga, idTransaksi)
                        }

                        // Rute QRIS Baru (Titip Beli)
                        composable("qris_payment") {
                            QrisPaymentScreen(
                                navController = navController,
                                namaLayanan = "Titip Beli",
                                totalHarga = "Rp 0",
                                idTransaksi = "TRX-000000"
                            )
                        }

                        // RUTE TRANSAKSI SAYA (MILIK USER)
                        composable("daftar_pesanan") {
                            DaftarPesananScreen(navController = navController, viewModel = pesananViewModel)
                        }

                        // RUTE DAFTAR PESANAN MASUK (MILIK MITRA / HELPER)
                        composable("daftar_pesanan_helper") {
                            DaftarPesananScreen(navController = navController)
                        }

                        composable("google") { PilihAkunScreen(navController) }
                        composable("daftar") { DaftarScreen(navController) }
                        composable("profil") { ProfileScreen(navController) }
                        composable("informasipribadi") { InformasiPribadiScreen(navController) }
                        composable("pesan") { PesanScreen(navController) }

                        composable("layarlokasi") {
                            LayarLokasi(
                                onAllowClick = {
                                    navController.navigate("beranda") { popUpTo("layarlokasi") { inclusive = true } }
                                },
                                onSkipClick = {
                                    navController.navigate("beranda") { popUpTo("layarlokasi") { inclusive = true } }
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
                            TrackingPesananScreen(navController, namaProduk, biayaStr.toIntOrNull() ?: 0)
                        }

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
                                onBayar = { navController.navigate("qris_payment") }
                            )
                        }

                        composable("tracking_mvvm") {
                            TrackingScreen(
                                viewModel = pesananViewModel,
                                onBack = {
                                    navController.navigate("beranda") {
                                        popUpTo("beranda") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // ====================================================================
                        // FIX TUNTAS: RUTE DETAIL PESANAN MITRA SEJARAH SEJAJAR DI NAVHOST
                        // ====================================================================
                        composable("detail_pesanan_mitra/{idTransaksi}") { backStackEntry ->
                            val idTransaksi = backStackEntry.arguments?.getString("idTransaksi") ?: ""
                            com.example.tolongin.screens.DetailPesananMitraScreen(
                                navController = navController,
                                idTransaksi = idTransaksi
                            )
                        }
                        // ====================================================================
                    }
                }
            }
        }
    }
}