package com.example.newscmp.ui.news

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.newscmp.domain.model.NewsItem
import com.example.newscmp.ui.SharedNewsViewModel
import newscmp.composeapp.generated.resources.Res
import newscmp.composeapp.generated.resources.loader
import org.jetbrains.compose.resources.painterResource

@Composable
fun NewsScreenRoot(
    viewModel: SharedNewsViewModel,
    onBackPressed: () -> Unit,
    modifier: Modifier,
) {
    val newsItem by viewModel.newsItem.collectAsStateWithLifecycle()
    newsItem?.let { newsItem ->
        NewsScreen(
            modifier = modifier,
            onAction = {
                when (it) {
                    NewsScreenEvent.OnBackButtonPressed -> onBackPressed()
                }
            },
            newsItem = newsItem,
        )
    }
}

@Composable
fun NewsScreen(
    newsItem: NewsItem,
    onAction: (NewsScreenEvent) -> Unit,
    modifier: Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        IconButton(onClick = {
            onAction(NewsScreenEvent.OnBackButtonPressed)
        }) {
            Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
        }

        Spacer(modifier = Modifier.size(8.dp))

        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(state = rememberScrollState())) {
            newsItem.title?.let {
                Text(
                    text = it,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AsyncImage(
                model = newsItem.imageUrl,
                placeholder = painterResource(Res.drawable.loader),
                error = painterResource(Res.drawable.loader),
                fallback = painterResource(Res.drawable.loader),
                modifier = Modifier.height(200.dp).fillMaxWidth(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = newsItem.content ?: "Empty news content",
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}