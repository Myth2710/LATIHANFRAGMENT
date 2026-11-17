package com.example.myapplication11

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    private lateinit var edtAngka1: EditText
    private lateinit var edtAngka2: EditText
    private lateinit var mainLayout: LinearLayout

    private val historyTambah = arrayListOf<String>()
    private val historyKurang = arrayListOf<String>()
    private val historyKali = arrayListOf<String>()
    private val historyBagi = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtAngka1 = findViewById(R.id.edtAngka1)
        edtAngka2 = findViewById(R.id.edtAngka2)
        mainLayout = findViewById(R.id.main)

        if (savedInstanceState != null) {
            historyTambah.addAll(savedInstanceState.getStringArrayList(KEY_TAMBAH) ?: arrayListOf())
            historyKurang.addAll(savedInstanceState.getStringArrayList(KEY_KURANG) ?: arrayListOf())
            historyKali.addAll(savedInstanceState.getStringArrayList(KEY_KALI) ?: arrayListOf())
            historyBagi.addAll(savedInstanceState.getStringArrayList(KEY_BAGI) ?: arrayListOf())
        }

        findViewById<Button>(R.id.btnTambah).setOnClickListener { hitung("+") }
        findViewById<Button>(R.id.btnKurang).setOnClickListener { hitung("-") }
        findViewById<Button>(R.id.btnKali).setOnClickListener { hitung("*") }
        findViewById<Button>(R.id.btnBagi).setOnClickListener { hitung("/") }

        if (savedInstanceState == null) {
            tampilkanFragment(Tambah(), historyTambah)
        } else {
            val operation = savedInstanceState.getString(KEY_OPERASI, "+")
            tampilkanFragment(fragmentUntuk(operation), historyUntuk(operation))
        }
    }

    private fun hitung(operator: String) {
        val angka1 = edtAngka1.text.toString().toDoubleOrNull()
        val angka2 = edtAngka2.text.toString().toDoubleOrNull()

        if (angka1 == null || angka2 == null) {
            Toast.makeText(this, "Masukkan kedua angka terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }
        if (operator == "/" && angka2 == 0.0) {
            Toast.makeText(this, "Angka tidak bisa dibagi 0", Toast.LENGTH_SHORT).show()
            return
        }

        val hasil = when (operator) {
            "+" -> angka1 + angka2
            "-" -> angka1 - angka2
            "*" -> angka1 * angka2
            else -> angka1 / angka2
        }

        val teksHasil = "${formatAngka(angka1)} $operator ${formatAngka(angka2)} = ${formatAngka(hasil)}"
        val history = historyUntuk(operator)
        history.add(teksHasil)

        mainLayout.setBackgroundColor(
            if (hasil < 0) android.graphics.Color.RED
            else android.graphics.Color.WHITE
        )
        tampilkanFragment(fragmentUntuk(operator), history)
    }

    private fun formatAngka(nilai: Double): String =
        BigDecimal.valueOf(nilai).stripTrailingZeros().toPlainString()

    private fun historyUntuk(operator: String): ArrayList<String> = when (operator) {
        "+" -> historyTambah
        "-" -> historyKurang
        "*" -> historyKali
        else -> historyBagi
    }

    private fun fragmentUntuk(operator: String): Fragment = when (operator) {
        "+" -> Tambah()
        "-" -> Kurang()
        "*" -> Kali()
        else -> Bagi()
    }

    private fun tampilkanFragment(fragment: Fragment, history: ArrayList<String>) {
        val bundle = Bundle().apply {
            putStringArrayList("history", ArrayList(history))
        }
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList(KEY_TAMBAH, historyTambah)
        outState.putStringArrayList(KEY_KURANG, historyKurang)
        outState.putStringArrayList(KEY_KALI, historyKali)
        outState.putStringArrayList(KEY_BAGI, historyBagi)
        val current = supportFragmentManager.findFragmentById(R.id.fragment_container)
        outState.putString(KEY_OPERASI, when (current) {
            is Kurang -> "-"
            is Kali -> "*"
            is Bagi -> "/"
            else -> "+"
        })
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val KEY_TAMBAH = "history_tambah"
        private const val KEY_KURANG = "history_kurang"
        private const val KEY_KALI = "history_kali"
        private const val KEY_BAGI = "history_bagi"
        private const val KEY_OPERASI = "operasi_terakhir"
    }
}
