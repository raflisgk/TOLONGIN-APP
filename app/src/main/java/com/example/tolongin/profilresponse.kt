package com.example.tolongin

import com.google.gson.annotations.SerializedName

data class ProfilResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: UserData?
)

data class UserData(
    @SerializedName("nama") val nama: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("telepon") val telepon: String?,
    @SerializedName("jenis_kelamin") val jenis_kelamin: String?, // Sesuaikan nama kolom DB
    @SerializedName("tanggal_lahir") val tanggal_lahir: String?, // Sesuaikan nama kolom DB
    @SerializedName("alamat") val alamat: String?
)