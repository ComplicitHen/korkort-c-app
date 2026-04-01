package com.korkort.cquiz

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.korkort.cquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        prefs = Prefs(this)

        binding.btnQuizAll.setOnClickListener {
            startQuiz("all")
        }

        binding.rvCategories.layoutManager = LinearLayoutManager(this)
        binding.rvCategories.adapter = CategoryAdapter(Prefs.CATEGORIES, prefs) { cat ->
            startQuiz(cat)
        }
    }

    override fun onResume() {
        super.onResume()
        updateStats()
        binding.rvCategories.adapter?.notifyDataSetChanged()
    }

    private fun updateStats() {
        val total = prefs.getTotalAnswered()
        val correct = prefs.getTotalCorrect()
        val pct = if (total > 0) correct * 100 / total else 0
        binding.tvStats.text = if (total == 0) "Inga frågor besvarade än"
        else "Totalt: $correct/$total rätt ($pct%)"
    }

    private fun startQuiz(category: String) {
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("category", category)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_reset) {
            AlertDialog.Builder(this)
                .setTitle("Nollställ statistik")
                .setMessage("Vill du nollställa all statistik?")
                .setPositiveButton("Ja") { _, _ ->
                    prefs.resetAll()
                    updateStats()
                    binding.rvCategories.adapter?.notifyDataSetChanged()
                    Toast.makeText(this, "Statistik nollställd", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Avbryt", null)
                .show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
