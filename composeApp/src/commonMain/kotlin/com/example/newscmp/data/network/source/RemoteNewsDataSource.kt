package com.example.newscmp.data.network.source

import com.example.newscmp.core.network.Error
import com.example.newscmp.core.network.Result
import com.example.newscmp.data.network.dto.NewsDto

interface RemoteNewsDataSource {
    suspend fun getNews(query: String) : Result<NewsDto, Error.NetworkError>
}