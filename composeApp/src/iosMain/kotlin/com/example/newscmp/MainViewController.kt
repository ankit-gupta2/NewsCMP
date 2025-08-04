package com.example.newscmp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.newscmp.di.initKoin

fun MainViewController() = ComposeUIViewController(configure = {
    initKoin()
}) { App() }