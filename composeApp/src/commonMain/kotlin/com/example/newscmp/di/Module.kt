package com.example.newscmp.di

import com.example.newscmp.AppDatabase
import com.example.newscmp.core.database.DriverFactory
import com.example.newscmp.data.DefaultNewsRepository
import com.example.newscmp.data.database.dao.NewsDaoImpl
import com.example.newscmp.data.database.source.LocalDataSource
import com.example.newscmp.data.database.source.SqlDelightDataSource
import com.example.newscmp.data.network.source.KtorRemoteNewsDataSource
import com.example.newscmp.data.network.source.RemoteNewsDataSource
import com.example.newscmp.domain.repository.NewsRepository
import com.example.newscmp.ui.SharedNewsViewModel
import com.example.newscmp.ui.home.HomeScreenViewModel
import com.example.newscmp.ui.search.SearchScreenViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {

    factory {
        val driver = get<DriverFactory>().getDriver()
        if (driver != null) {
            val queries = AppDatabase.invoke(driver).newsEntityQueries
            SqlDelightDataSource(dao = NewsDaoImpl(queries))
        } else {
            SqlDelightDataSource(dao = null)
        }
    }.bind(LocalDataSource::class)

    single<HttpClient> {
        HttpClient(engine = get()) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 20_000
                socketTimeoutMillis = 20_000
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }
    factory { KtorRemoteNewsDataSource(httpClient = get()) }.bind(RemoteNewsDataSource::class)
    factory { DefaultNewsRepository(remoteDataSource = get(), localDataSource = get()) }.bind(
        NewsRepository::class
    )

    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::SharedNewsViewModel)
    viewModelOf(::SearchScreenViewModel)
}
