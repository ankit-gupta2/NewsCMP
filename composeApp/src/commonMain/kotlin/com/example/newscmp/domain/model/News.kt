package com.example.newscmp.domain.model

data class NewsItem(
    val id: String,
    val title: String?,
    val description: String?,
    val content: String?,
    val date: String?,
    val imageUrl: String?,
    val sourceName: String?,
    val sourceIconUrl: String?,
)

enum class NewsCategory {
    Latest,
    Business,
    Technology,
    Health,
    Science,
    Politics,
    Sports,
    Entertainment,
    Crime,
    Weather,
    Education,
    World,
    Economy,
    Opinion,
    Lifestyle,
}