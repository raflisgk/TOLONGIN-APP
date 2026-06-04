package com.example.tolongin.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale

data class DetailPengirimanState(
    val orderId: String = "TRX-88291",
    val lokasiJemput: String = "",
    val lokasiTujuan: String = "",
    val jenisPaket: String = "Paket Kecil",
    val beratPaket: String = "",
    val deskripsiBarang: String = "",
    val waktuJemput: String = "Sekarang (Langsung Jemput)",
    val namaPenerima: String = "",
    val nomorHP: String = "",
    val catatanHelper: String = "",
    val status: String = "Dalam Pengiriman",
    val helperName: String = "Budi Darmawan",
    val helperRating: String = "4.9",
    val helperSpesialis: String = "Spesialis Barang Besar & Fragile",
    val jarak: Double = 4.2,
    val biayaPengiriman: Int = 10000,
    val biayaAsuransi: Int = 2000,
    val biayaPlatform: Int = 1000,
    val totalBayarFormatted: String = "Rp 13.000"
)

class PesananViewModel : ViewModel() {
    private val _pesanan = MutableStateFlow(DetailPengirimanState())
    val pesanan: StateFlow<DetailPengirimanState> = _pesanan.asStateFlow()

    fun updateLokasi(jemput: String, tujuan: String) {
        _pesanan.value = _pesanan.value.copy(lokasiJemput = jemput, lokasiTujuan = tujuan)
    }

    // ====================================================================
    // FIX: Diberi nilai default = 10000 agar fungsi Preview lama tidak crash!
    // ====================================================================
    fun updateJenisPaket(jenis: String, berat: String, hargaOngkir: Int = 10000) {
        val total = hargaOngkir + _pesanan.value.biayaAsuransi + _pesanan.value.biayaPlatform
        val format = NumberFormat.getNumberInstance(Locale("id", "ID"))

        _pesanan.value = _pesanan.value.copy(
            jenisPaket = jenis,
            beratPaket = berat,
            biayaPengiriman = hargaOngkir,
            totalBayarFormatted = "Rp ${format.format(total)}"
        )
    }

    fun updateDetailBarang(deskripsi: String) { _pesanan.value = _pesanan.value.copy(deskripsiBarang = deskripsi) }
    fun updateWaktu(waktu: String) { _pesanan.value = _pesanan.value.copy(waktuJemput = waktu) }
    fun updatePenerima(nama: String, hp: String) { _pesanan.value = _pesanan.value.copy(namaPenerima = nama, nomorHP = hp) }
    fun updateCatatan(catatan: String) { _pesanan.value = _pesanan.value.copy(catatanHelper = catatan) }
}