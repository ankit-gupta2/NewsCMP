package com.example.newscmp.data.database.dao

import com.example.newscmp.NewsEntity
import com.example.newscmp.data.network.dto.NewsItemDto

interface NewsDao {
    suspend fun insertNewsItem(newsItemDto: NewsItemDto, category: String?)
    suspend fun getAllNewsItems(category: String?): List<NewsEntity>
    suspend fun getAllNewsItemsCount(category: String?): Long
}