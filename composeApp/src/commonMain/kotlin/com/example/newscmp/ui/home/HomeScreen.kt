package com.example.newscmp.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.newscmp.domain.model.NewsCategory
import com.example.newscmp.domain.model.NewsItem
import newscmp.composeapp.generated.resources.Res
import newscmp.composeapp.generated.resources.app_name
import newscmp.composeapp.generated.resources.loader
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenRoot(
    homeScreenViewModel: HomeScreenViewModel = koinViewModel(),
    onNewsItemClicked: (NewsItem) -> Unit,
    onSearchButtonClicked: () -> Unit,
    modifier: Modifier,
) {
    val state by homeScreenViewModel.state.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        onAction = { event ->
            when (event) {
                is HomeScreenEvent.OnNewsItemClicked -> {
                    onNewsItemClicked(event.newsItem)
                }

                is HomeScreenEvent.OnNewsCategoryChanged -> Unit
                HomeScreenEvent.OnSearchButtonClicked -> {
                    onSearchButtonClicked()
                }
            }

            homeScreenViewModel.onEvent(event)
        },
        modifier = modifier,
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    onAction: (HomeScreenEvent) -> Unit
) {
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Header(
            onAction = onAction,
        )

        CategoryChips(
            modifier = Modifier.fillMaxWidth(),
            onAction = onAction,
            selectedCategory = state.selectedNewsCategory
        )

        Crossfade(
            targetState = state.isLoading,
            modifier = Modifier.fillMaxSize()
        ) { isLoading ->
            if (isLoading) {
                Loader()
            } else {
                NewsList(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    newsItems = state.newsItems,
                    onAction = onAction,
                )
            }
        }

    }
}

@Composable
fun Loader() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun Header(
    onAction: (HomeScreenEvent) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(Res.string.app_name),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        IconButton(
            onClick = {
            onAction(HomeScreenEvent.OnSearchButtonClicked)
            }
        ) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        }
    }

}

@Composable
fun NewsList(
    newsItems: List<NewsItem>,
    onAction: (HomeScreenEvent) -> Unit,
    modifier: Modifier,
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(items = newsItems, key = { it.id }) { item ->
            NewsItem(
                item = item,
                onClick = {
                    onAction(HomeScreenEvent.OnNewsItemClicked(newsItem = item))
                }
            )
        }
    }
}

@Composable
fun NewsItem(
    item: NewsItem,
    onClick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth().clickable(enabled = true, onClick = onClick)) {
        Column(modifier = Modifier.weight(1f).padding(vertical = 8.dp)) {
            item.title?.let { title ->
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.size(4.dp))
            }

            item.description?.let { description ->
                Text(
                    text = description,
                    fontSize = 20.sp,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.size(4.dp))
            }
        }

        item.imageUrl?.let { url ->
            Spacer(modifier = Modifier.size(16.dp))

            AsyncImage(
                model = url,
                placeholder = painterResource(Res.drawable.loader),
                error = painterResource(Res.drawable.loader),
                fallback = painterResource(Res.drawable.loader),
                contentDescription = null,
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
fun CategoryChips(
    modifier: Modifier,
    onAction: (HomeScreenEvent) -> Unit,
    selectedCategory: NewsCategory?,
) {
    LazyRow(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp), contentPadding = PaddingValues(16.dp)) {
        items(items = NewsCategory.entries, key = { it }) { category ->
            CategoryChip(
                label = category.name,
                onClick = {
                    onAction(HomeScreenEvent.OnNewsCategoryChanged(newsCategory = category))
                },
                isSelected = category == selectedCategory,
            )
        }
    }
}

@Composable
fun CategoryChip(
    label: String,
    onClick: () -> Unit,
    isSelected: Boolean,
) {
    val modifier = remember(isSelected) {
        if (isSelected) {
            Modifier.background(color = Color(0xFF789ABC), shape = RoundedCornerShape(50))
                .border(.5.dp, Color.Black, shape = RoundedCornerShape(50))
        } else {
            Modifier.background(color = Color(0xFFDfDFDF), shape = RoundedCornerShape(50))
        }
    }

    Box(
        modifier = modifier.clickable(enabled = true, onClick = onClick)
    ) {
        Text(label, modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp))
    }
}
