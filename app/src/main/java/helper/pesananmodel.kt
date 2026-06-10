package com.example.tolongin

import com.google.gson.annotations.SerializedName

data class PesananModel(
    @SerializedName("id_transaksi") val id_transaksi: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("nama_layanan") val nama_layanan: String = "",
    @SerializedName("tanggal") val tanggal: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("total_harga") val total_harga: String = "",

    // Data opsional (Bisa NULL di database)
    @SerializedName("foto_sebelum") val foto_sebelum: String? = null,
    @SerializedName("foto_sesudah") val foto_sesudah: String? = null,
    @SerializedName("catatan") val catatan: String? = null,
    @SerializedName("email_helper") val email_helper: String? = null,
    @SerializedName("nama_produk") val nama_produk: String? = null,
    @SerializedName("jumlah") val jumlah: Int? = 1,
    @SerializedName("alamat_tujuan") val alamat_tujuan: String? = null,
    @SerializedName("lokasi_jemput") val lokasi_jemput: String? = null,
    @SerializedName("lokasi_tujuan") val lokasi_tujuan: String? = null,
    @SerializedName("jenis_paket") val jenis_paket: String? = null,
    @SerializedName("deskripsi_barang") val deskripsi_barang: String? = null,
    @SerializedName("waktu_jemput") val waktu_jemput: String? = null,
    @SerializedName("nama_penerima") val nama_penerima: String? = null,
    @SerializedName("no_hp_penerima") val no_hp_penerima: String? = null,
    @SerializedName("catatan_helper") val catatan_helper: String? = null
)