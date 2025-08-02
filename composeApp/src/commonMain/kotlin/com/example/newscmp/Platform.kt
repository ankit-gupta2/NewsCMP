package com.example.newscmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform