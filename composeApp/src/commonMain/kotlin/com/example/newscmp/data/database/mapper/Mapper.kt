package com.example.newscmp.data.database.mapper

import com.example.newscmp.NewsEntity
import com.example.newscmp.domain.model.NewsItem


fun NewsEntity.toNewsItem(): NewsItem {
    return NewsItem(
        id = id,
        title = title,
        description = description,
        content = content,
        date = date,
        imageUrl = imageUrl,
        sourceName = sourceName,
        sourceIconUrl = sourceIconUrl,
    )
}