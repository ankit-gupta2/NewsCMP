package com.example.newscmp.data.database.source

import com.example.newscmp.data.database.dao.NewsDao
import com.example.newscmp.data.database.mapper.toNewsItem
import com.example.newscmp.data.network.dto.NewsItemDto
import com.example.newscmp.domain.model.NewsItem

class SqlDelightDataSource(private val dao: NewsDao?) : LocalDataSource {
    override suspend fun insertNewsItemDto(newsItemDto: NewsItemDto, category: String?) {
        dao?.insertNewsItem(newsItemDto, category)
    }

    override suspend fun getAllNewsItemsForCategory(category: String?): List<NewsItem>? {
        return dao?.getAllNewsItems(category)?.map {
            it.toNewsItem()
        }
    }

    override suspend fun getAllNewsItemsForCategoryCount(category: String?): Long {
        return dao?.getAllNewsItemsCount(category) ?: -1
    }
}