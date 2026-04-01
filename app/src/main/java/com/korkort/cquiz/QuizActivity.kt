package com.korkort.cquiz

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.korkort.cquiz.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var prefs: Prefs
    private lateinit var questions: List<Question>
    private var currentIndex = 0
    private var score = 0
    private var answered = false
    private lateinit var category: String
    private val optionButtons: List<Button> get() =
        listOf(binding.btnA, binding.btnB, binding.btnC, binding.btnD)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = Prefs(this)
        category = intent.getStringExtra("category") ?: "all"

        questions = if (category == "all") {
            QuestionLoader.all(this).take(20)
        } else {
            QuestionLoader.forCategory(this, category)
        }

        if (questions.isEmpty()) {
            finish()
            return
        }

        supportActionBar?.title = if (category == "all") "Blandat quiz"
            else Prefs.CATEGORIES[category] ?: category
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        showQuestion()

        optionButtons.forEachIndexed { i, btn ->
            btn.setOnClickListener { onAnswer(i) }
        }

        binding.btnNext.setOnClickListener { nextQuestion() }
    }

    private fun showQuestion() {
        answered = false
        val q = questions[currentIndex]
        binding.tvProgress.text = "Fråga ${currentIndex + 1} / ${questions.size}"
        binding.progressQuiz.max = questions.size
        binding.progressQuiz.progress = currentIndex + 1
        binding.tvQuestion.text = q.question
        binding.tvExplanation.visibility = View.GONE
        binding.btnNext.visibility = View.GONE

        optionButtons.forEachIndexed { i, btn ->
            btn.text = q.options.getOrNull(i) ?: ""
            btn.visibility = if (i < q.options.size) View.VISIBLE else View.GONE
            btn.isEnabled = true
            btn.setBackgroundColor(getColor(com.google.android.material.R.color.m3_sys_color_dynamic_light_primary))
            btn.setTextColor(Color.WHITE)
        }

        // reset to default button color
        optionButtons.forEach { btn ->
            btn.background = getDrawable(R.drawable.btn_option_default)
            btn.setTextColor(getColor(R.color.option_text))
        }
    }

    private fun onAnswer(selectedIndex: Int) {
        if (answered) return
        answered = true
        val q = questions[currentIndex]
        val correct = q.answer == selectedIndex

        if (correct) score++
        prefs.recordAnswer(q.category, correct)

        optionButtons.forEachIndexed { i, btn ->
            btn.isEnabled = false
            when {
                i == q.answer -> btn.setBackgroundColor(Color.parseColor("#388E3C"))
                i == selectedIndex && !correct -> btn.setBackgroundColor(Color.parseColor("#D32F2F"))
                else -> btn.alpha = 0.5f
            }
            btn.setTextColor(Color.WHITE)
        }

        binding.tvExplanation.text = q.explanation
        binding.tvExplanation.visibility = View.VISIBLE

        if (currentIndex < questions.size - 1) {
            binding.btnNext.visibility = View.VISIBLE
        } else {
            Handler(Looper.getMainLooper()).postDelayed({ showResult() }, 1500)
        }
    }

    private fun nextQuestion() {
        currentIndex++
        optionButtons.forEach { it.alpha = 1f }
        showQuestion()
    }

    private fun showResult() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("score", score)
        intent.putExtra("total", questions.size)
        intent.putExtra("category", category)
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
