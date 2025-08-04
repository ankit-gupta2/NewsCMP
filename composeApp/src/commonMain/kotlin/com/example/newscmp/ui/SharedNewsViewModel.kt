package com.example.newscmp.ui

import androidx.lifecycle.ViewModel
import com.example.newscmp.domain.model.NewsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SharedNewsViewModel: ViewModel() {
    private val _newsItem = MutableStateFlow<NewsItem?>(null)
    val newsItem = _newsItem.asStateFlow()

    fun updateNewsItem(newsItem: NewsItem) {
        _newsItem.update {
            newsItem
        }
    }
}