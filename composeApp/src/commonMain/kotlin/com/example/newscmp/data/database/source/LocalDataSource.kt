package com.example.newscmp.data.database.source

import com.example.newscmp.data.network.dto.NewsItemDto
import com.example.newscmp.domain.model.NewsItem

interface LocalDataSource {
    suspend fun insertNewsItemDto(newsItemDto: NewsItemDto, category: String?)
    suspend fun getAllNewsItemsForCategory(category: String?) : List<NewsItem>?
    suspend fun getAllNewsItemsForCategoryCount(category: String?) : Long
}