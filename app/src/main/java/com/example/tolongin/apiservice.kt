package com.example.tolongin

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("tolongin_api/register.php")
    fun registerUser(
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("tolongin_api/login.php")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("tolongin_api/get_profile.php")
    fun getProfil(
        @Field("email") email: String
    ): Call<ProfilResponse>

    @FormUrlEncoded
    @POST("tolongin_api/update_profile.php")
    fun updateProfil(
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("telepon") telepon: String,
        @Field("jenis_kelamin") jenis_kelamin: String,
        @Field("tanggal_lahir") tanggal_lahir: String,
        @Field("alamat") alamat: String
    ): Call<ResponseModel>

    // SIMPAN TRANSAKSI
    @FormUrlEncoded
    @POST("tolongin_api/insert_transaksi.php")
    fun simpanPemesanan(
        @Field("id_transaksi") idTransaksi: String,
        @Field("email") email: String,
        @Field("nama_layanan") namaLayanan: String,
        @Field("tanggal") tanggal: String,
        @Field("status") status: String,
        @Field("total_harga") totalHarga: String
    ): Call<ResponseModel>

    // SIMPAN PESANAN
    @FormUrlEncoded
    @POST("tolongin_api/insert_pesanan.php")
    fun insertPesanan(
        @Field("nama_layanan") namaLayanan: String,
        @Field("harga") harga: String,
        @Field("tanggal") tanggal: String,
        @Field("alamat") alamat: String,
        @Field("waktu") waktu: String,
        @Field("status") status: String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("tolongin_api/insert_titip_beli.php")
    fun insertTitipBeli(
        @Field("id_transaksi") idTransaksi: String,
        @Field("email") email: String,
        @Field("nama_layanan") namaLayanan: String,
        @Field("tanggal") tanggal: String,
        @Field("status") status: String,
        @Field("total_harga") totalHarga: String
    ): Call<ResponseModel>

    // AMBIL DATA PESANAN (FIXED: Tanda kurung parameter sudah ditutup dengan benar)
    @FormUrlEncoded
    @POST("tolongin_api/get_pesanan.php")
    fun getPesanan(
        @Field("email") email: String
    ): Call<List<PesananModel>>
}

// ====================================================================
// MODEL ASLI: Ditempatkan di luar interface & Sesuai Kolom Database MySQL
// ====================================================================
data class PesananModel(
    val id_transaksi: String = "",
    val email: String = "",
    val nama_layanan: String = "",
    val tanggal: String = "",
    val status: String = "",
    val total_harga: String = ""
)