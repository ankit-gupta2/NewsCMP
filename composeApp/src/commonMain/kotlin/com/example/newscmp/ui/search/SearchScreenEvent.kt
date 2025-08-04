package com.example.newscmp.ui.search

import com.example.newscmp.domain.model.NewsItem

sealed interface SearchScreenEvent {
    data class OnSearchQueryChanged(val query: String): SearchScreenEvent
    data object OnBackPressed: SearchScreenEvent
    data class OnNewsItemClicked(val newsItem: NewsItem): SearchScreenEvent
}