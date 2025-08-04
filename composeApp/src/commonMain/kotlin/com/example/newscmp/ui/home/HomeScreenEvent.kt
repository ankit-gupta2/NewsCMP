package com.example.newscmp.ui.home

import com.example.newscmp.domain.model.NewsCategory
import com.example.newscmp.domain.model.NewsItem

sealed interface HomeScreenEvent {
    data object OnSearchButtonClicked: HomeScreenEvent
    data class OnNewsCategoryChanged(val newsCategory: NewsCategory) : HomeScreenEvent
    data class OnNewsItemClicked(val newsItem: NewsItem) : HomeScreenEvent
}