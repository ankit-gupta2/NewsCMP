package com.example.newscmp

import android.app.Application
import com.example.newscmp.di.initKoin
import org.koin.android.ext.koin.androidContext

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@NewsApplication)
        }
    }
}