package com.korkort.cquiz

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("korkort_prefs", Context.MODE_PRIVATE)

    fun getCorrect(category: String): Int = prefs.getInt("correct_$category", 0)
    fun getTotal(category: String): Int = prefs.getInt("total_$category", 0)

    fun recordAnswer(category: String, correct: Boolean) {
        val c = getCorrect(category) + if (correct) 1 else 0
        val t = getTotal(category) + 1
        prefs.edit().putInt("correct_$category", c).putInt("total_$category", t).apply()
    }

    fun getTotalCorrect(): Int = CATEGORIES.keys.sumOf { getCorrect(it) }
    fun getTotalAnswered(): Int = CATEGORIES.keys.sumOf { getTotal(it) }

    fun resetCategory(category: String) {
        prefs.edit().putInt("correct_$category", 0).putInt("total_$category", 0).apply()
    }

    fun resetAll() {
        prefs.edit().clear().apply()
    }

    companion object {
        val CATEGORIES = mapOf(
            "trafikregler" to "Trafikregler",
            "fordon" to "Fordonskännedom",
            "lastsäkring" to "Lastsäkring",
            "vilotider" to "Kör- & vilotider",
            "sakerhet" to "Säkerhetskontroll",
            "miljo" to "Miljö & Ekonomi",
            "adr" to "Farligt gods (ADR)",
            "nod" to "Nödsituationer"
        )
    }
}
