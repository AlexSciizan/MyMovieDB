package com.app.mymoviedb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        /*
        1. Buat layar untuk menampilkan daftar genre film resmi.
        2. Buat layar untuk daftar film yang ditemukan berdasarkan genre.
        3. Tampilkan informasi utama tentang film saat pengguna mengklik film tersebut.
        4. Tampilkan ulasan pengguna untuk sebuah film.
        5. Tampilkan trailer youtube dari film tersebut.
        6. Terapkan pengguliran tanpa akhir pada daftar film dan ulasan pengguna.
        7. Menutupi kasus positif dan negatif.
        */
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}