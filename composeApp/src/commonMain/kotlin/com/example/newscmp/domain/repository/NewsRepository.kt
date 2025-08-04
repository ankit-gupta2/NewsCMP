package com.example.newscmp.domain.repository

import com.example.newscmp.core.network.Error
import com.example.newscmp.core.network.Result
import com.example.newscmp.domain.model.NewsItem

interface NewsRepository {
    suspend fun getNewsItems(query: String, cache: Boolean) : Result<List<NewsItem>, Error>
}