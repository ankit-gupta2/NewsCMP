package com.example.newscmp.data.database.dao

import com.example.newscmp.NewsEntity
import com.example.newscmp.NewsEntityQueries
import com.example.newscmp.core.coroutine.ioDispatcher
import com.example.newscmp.data.network.dto.NewsItemDto
import kotlinx.coroutines.withContext

class NewsDaoImpl(private val queries: NewsEntityQueries) : NewsDao {
    override suspend fun insertNewsItem(newsItemDto: NewsItemDto, category: String?) {
        withContext(ioDispatcher) {
            queries.insertNewsEntity(
                id = newsItemDto.articleId,
                title = newsItemDto.title,
                description = newsItemDto.description,
                content = newsItemDto.content,
                date = newsItemDto.date,
                imageUrl = newsItemDto.imageUrl,
                sourceName = newsItemDto.sourceName,
                sourceIconUrl = newsItemDto.sourceIconUrl,
                category = category,
            )
        }
    }

    override suspend fun getAllNewsItems(category: String?): List<NewsEntity> {
        return withContext(ioDispatcher) {
            queries.getAllNewsItemsForCategory(category).executeAsList()
        }
    }

    override suspend fun getAllNewsItemsCount(category: String?): Long {
        return withContext(ioDispatcher) {
            queries.getCountForAllNewsItemsForCategory(category).executeAsOne()
        }
    }
}