package com.example.tolongin

import com.google.gson.annotations.SerializedName

data class PesananModel(
    @SerializedName("id_transaksi")
    val id_transaksi: String = "",

    @SerializedName("email")
    val email: String = "",

    @SerializedName("nama_layanan")
    val nama_layanan: String = "",

    @SerializedName("tanggal")
    val tanggal: String = "",

    @SerializedName("status")
    val status: String = "",

    @SerializedName("total_harga")
    val total_harga: String = "",

    // ── TAMBAHKAN BARIS INI ──
    @SerializedName("email_helper")
    val email_helper: String? = null
)