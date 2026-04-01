package com.korkort.cquiz

data class Question(
    val id: String,
    val category: String,
    val question: String,
    val options: List<String>,
    val answer: Int,
    val explanation: String
)
