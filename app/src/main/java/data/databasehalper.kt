package data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "TolongIn.db", null, 2) {
    override fun onCreate(db: SQLiteDatabase) {
        // 1. Tabel Pesanan (Yang sudah kita buat sebelumnya)
        db.execSQL("""
            CREATE TABLE pesanan (
                id_transaksi TEXT PRIMARY KEY,
                nama_layanan TEXT,
                harga TEXT,
                alamat TEXT,
                tanggal TEXT,
                status TEXT
            )
        """)

        // 2. Tabel Users (BARU: Untuk Login)
        db.execSQL("""
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT UNIQUE,
                password TEXT
            )
        """)

        // Memasukkan akun dummy langsung saat database pertama kali dibuat untuk testing
        db.execSQL("INSERT INTO users (email, password) VALUES ('admin@gmail.com', 'admin123')")
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.execSQL("DROP TABLE IF EXISTS pesanan")
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    // ================= FUNGSI TRANSAKSI =================
    fun tambahPesanan(id: String, layanan: String, harga: String, alamat: String, tgl: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id_transaksi", id)
            put("nama_layanan", layanan)
            put("harga", harga)
            put("alamat", alamat)
            put("tanggal", tgl)
            put("status", "Pending")
        }
        db.insert("pesanan", null, values)
        db.close()
    }

    // ================= FUNGSI AUTENTIKASI (LOGIN) =================
    // Fungsi untuk mengecek apakah email dan password cocok di database
    fun cekLogin(email: String, sandi: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND password=?", arrayOf(email, sandi))
        val isValid = cursor.count > 0
        cursor.close()
        return isValid
    }

    // Fungsi untuk mendaftarkan user baru (Nanti dipanggil di halaman Daftar)
    fun daftarUser(email: String, sandi: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("email", email)
            put("password", sandi)
        }
        val result = db.insert("users", null, values)
        db.close()
        return result != -1L
    }
}