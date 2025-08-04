package com.example.newscmp.data.network.dto

import com.example.newscmp.domain.model.NewsItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsDto(
    val results: List<NewsItemDto>,
)

@Serializable
data class NewsItemDto(
    @SerialName("article_id")
    val articleId: String,
    val title: String?,
    val description: String?,
    val content: String?,
    @SerialName("pubDate")
    val date: String?,
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("source_name")
    val sourceName: String?,
    @SerialName("source_icon")
    val sourceIconUrl: String?,
) {
    fun toNewsItem() : NewsItem {
        return NewsItem(
            id = articleId,
            title = title,
            description = description,
            content = content,
            date = date,
            imageUrl = imageUrl,
            sourceName = sourceName,
            sourceIconUrl = sourceIconUrl,
        )
    }
}