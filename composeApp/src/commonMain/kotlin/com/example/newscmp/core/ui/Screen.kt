package com.example.newscmp.core.ui

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    data object NewsGraph : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data object News : Screen
}