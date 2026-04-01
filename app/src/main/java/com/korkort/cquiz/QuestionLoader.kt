package com.korkort.cquiz

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object QuestionLoader {
    private var questions: List<Question>? = null

    fun load(context: Context): List<Question> {
        if (questions != null) return questions!!
        val json = context.assets.open("questions.json").bufferedReader().readText()
        val type = object : TypeToken<List<Question>>() {}.type
        questions = Gson().fromJson(json, type)
        return questions!!
    }

    fun forCategory(context: Context, category: String): List<Question> =
        load(context).filter { it.category == category }.shuffled()

    fun all(context: Context): List<Question> = load(context).shuffled()
}
