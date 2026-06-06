package com.example.tolongin

data class ResponseModel(
    val status: String,
    val message: String,

    // ─── TAMBAHAN BARIS BARU DI SINI ───
    val role: String? = null
)