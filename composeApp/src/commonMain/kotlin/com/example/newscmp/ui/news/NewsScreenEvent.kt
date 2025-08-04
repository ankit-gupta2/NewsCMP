package com.example.newscmp.ui.news

sealed interface NewsScreenEvent {
    data object OnBackButtonPressed: NewsScreenEvent
}