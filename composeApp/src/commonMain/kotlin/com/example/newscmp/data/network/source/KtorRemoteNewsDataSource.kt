package com.example.newscmp.data.network.source

import com.example.newscmp.core.network.Error
import com.example.newscmp.core.network.Result
import com.example.newscmp.core.network.safeCall
import com.example.newscmp.data.network.dto.NewsDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

private const val BASE_URL = "https://newsdata.io/"
private const val LATEST_NEWS_API = "api/1/latest"

class KtorRemoteNewsDataSource(private val httpClient: HttpClient) : RemoteNewsDataSource {
    override suspend fun getNews(query: String): Result<NewsDto, Error.NetworkError> {
        return safeCall {
            httpClient.get(
                urlString = BASE_URL + LATEST_NEWS_API
            ) {
                parameter("apikey", "pub_48c896f622d24c56a2d444b7526fcc7c")
                parameter("q", query)
            }
        }
    }
}