package com.example.tolongin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tolongin.data.PesananModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PesananViewModel : ViewModel() {
    private val _pesanan = MutableStateFlow(PesananModel())
    val pesanan: StateFlow<PesananModel> = _pesanan.asStateFlow()

    fun updateLokasi(jemput: String, tujuan: String) {
        _pesanan.value = _pesanan.value.copy(
            lokasiJemput = jemput,
            lokasiTujuan = tujuan
        )
    }

    fun updateJenisPaket(jenis: String, berat: String) {
        _pesanan.value = _pesanan.value.copy(
            jenisPaket = jenis,
            beratPaket = berat
        )
    }

    fun updateDetailBarang(deskripsi: String) {
        _pesanan.value = _pesanan.value.copy(deskripsiBarang = deskripsi)
    }

    fun updateWaktu(waktu: String) {
        _pesanan.value = _pesanan.value.copy(waktuJemput = waktu)
    }

    fun updatePenerima(nama: String, hp: String) {
        _pesanan.value = _pesanan.value.copy(
            namaPenerima = nama,
            nomorHP = hp
        )
    }

    fun updateCatatan(catatan: String) {
        _pesanan.value = _pesanan.value.copy(catatanHelper = catatan)
    }
}