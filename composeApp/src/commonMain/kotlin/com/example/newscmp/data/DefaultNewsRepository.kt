package com.example.newscmp.data

import com.example.newscmp.core.network.Error
import com.example.newscmp.core.network.Result
import com.example.newscmp.core.network.map
import com.example.newscmp.data.database.source.LocalDataSource
import com.example.newscmp.data.network.source.RemoteNewsDataSource
import com.example.newscmp.domain.model.NewsItem
import com.example.newscmp.domain.repository.NewsRepository

class DefaultNewsRepository(
    private val remoteDataSource: RemoteNewsDataSource,
    private val localDataSource: LocalDataSource,
) : NewsRepository {
    override suspend fun getNewsItems(
        query: String,
        cache: Boolean
    ): Result<List<NewsItem>, Error> {
        val count = localDataSource.getAllNewsItemsForCategoryCount(query)

        return if (count > 0) {
            localDataSource.getAllNewsItemsForCategory(query).let {
                Result.Success(data = it ?: emptyList())
            }
        } else {
            remoteDataSource.getNews(query).map {
                it.results.map {
                    if (cache) {
                        localDataSource.insertNewsItemDto(newsItemDto = it, category = query)
                    }
                    it.toNewsItem()
                }
            }
        }
    }
}