package com.example.tolongin.data

data class PesananModel(
    val lokasiJemput: String = "",
    val lokasiTujuan: String = "",
    val jenisPaket: String = "Paket Kecil",
    val beratPaket: String = "MAKS 5 KG",
    val deskripsiBarang: String = "",
    val waktuJemput: String = "Sekarang (Segera)",
    val namaPenerima: String = "",
    val nomorHP: String = "",
    val catatanHelper: String = "",
    val helperName: String = "Budi Darmawan",
    val helperRating: String = "4.9",
    val helperSpesialis: String = "Kurir Kilat & Fragile Specialist",
    val jarak: String = "3.4",
    val biayaPengiriman: Int = 12000,
    val biayaAsuransi: Int = 2500,
    val biayaPlatform: Int = 1000,
    val orderId: String = "#TIN-982347"
) {
    val totalBayar: Int get() = biayaPengiriman + biayaAsuransi + biayaPlatform
    val totalBayarFormatted: String get() = "Rp ${"%,d".format(totalBayar).replace(',', '.')}"
}