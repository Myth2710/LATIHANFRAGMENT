package com.example.myapplication11

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

open class History(
    private val judul: String
) : Fragment(R.layout.history) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val daftarHistory = arguments?.getStringArrayList("history") ?: arrayListOf()
        view.findViewById<TextView>(R.id.txtJudulHistory).text = judul
        view.findViewById<TextView>(R.id.txtHistory).text =
            if (daftarHistory.isEmpty()) "Belum ada history operasi"
            else daftarHistory.mapIndexed { index, item -> "${index + 1}. $item" }.joinToString("\n")
    }
}
