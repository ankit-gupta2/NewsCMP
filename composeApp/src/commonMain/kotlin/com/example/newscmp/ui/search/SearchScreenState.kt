package com.example.newscmp.ui.search

import com.example.newscmp.domain.model.NewsItem

data class SearchScreenState(
    val newsItems: List<NewsItem> = emptyList(),
    val isLoading: Boolean = false,
    val label: String = "Stay updated with latest news...",
    val searchQuery: String = "",
)