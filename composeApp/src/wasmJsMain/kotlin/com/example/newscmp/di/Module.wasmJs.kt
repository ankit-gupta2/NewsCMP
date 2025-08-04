package com.example.newscmp.di

import com.example.newscmp.core.database.DriverFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.JsClient
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { JsClient().create() }
        factory { DriverFactory() }
    }