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

    @FormUrlEncoded
    @POST("tolongin_api/get_pesanan.php")
    fun getPesanan(
        @Field("email") email: String
    ): Call<List<PesananModel>>

    @FormUrlEncoded
    @POST("tolongin_api/ambil_pesanan.php")
    fun ambilPesanan(
        @Field("id_transaksi") idTransaksi: String,
        @Field("email_helper") emailHelper: String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("tolongin_api/update_pesanan.php")
    fun kirimLaporanForm(
        @Field("id_transaksi") idTransaksi: String,
        @Field("status") status: String,
        @Field("foto_sebelum") fotoSebelum: String?,
        @Field("foto_sesudah") fotoSesudah: String?,
        @Field("catatan") catatan: String?
    ): Call<ResponseModel>
}