package com.korkort.cquiz

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.korkort.cquiz.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 1)
        val category = intent.getStringExtra("category") ?: "all"
        val pct = score * 100 / total

        binding.tvScore.text = "$score / $total"
        binding.tvPercent.text = "$pct%"
        binding.progressResult.progress = pct

        val (msg, color) = when {
            pct >= 85 -> "Utmärkt! Godkänd!" to Color.parseColor("#388E3C")
            pct >= 70 -> "Bra jobbat! Nästan godkänd." to Color.parseColor("#F57C00")
            else -> "Fortsätt öva – du klarar det!" to Color.parseColor("#D32F2F")
        }
        binding.tvMessage.text = msg
        binding.tvMessage.setTextColor(color)

        binding.btnRetry.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("category", category)
            startActivity(intent)
            finish()
        }

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}
