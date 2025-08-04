package com.example.newscmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.newscmp.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "NewsCMP",
    ) {
        App()
    }
}