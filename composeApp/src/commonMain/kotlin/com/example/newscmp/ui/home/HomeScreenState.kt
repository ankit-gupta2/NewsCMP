package com.example.newscmp.ui.home

import com.example.newscmp.domain.model.NewsCategory
import com.example.newscmp.domain.model.NewsItem

data class HomeScreenState(
    val newsItems : List<NewsItem> = emptyList(),
    val selectedNewsCategory: NewsCategory = NewsCategory.Latest,
    val isLoading: Boolean = true,
    val errorLabel: String = "",
)
