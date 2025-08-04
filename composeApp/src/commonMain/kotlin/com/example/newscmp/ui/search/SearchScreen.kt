package com.example.newscmp.ui.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newscmp.domain.model.NewsItem
import com.example.newscmp.ui.home.NewsItem
import newscmp.composeapp.generated.resources.Res
import newscmp.composeapp.generated.resources.search_placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
fun SearchScreenRoot(
    viewModel: SearchScreenViewModel,
    onBackPressed: () -> Unit,
    onNewsItemClicked: (NewsItem) -> Unit,
    modifier: Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SearchScreen(
        state = state,
        modifier = modifier,
        onAction = { event ->
            when (event) {
                SearchScreenEvent.OnBackPressed -> onBackPressed()
                is SearchScreenEvent.OnNewsItemClicked -> onNewsItemClicked(event.newsItem)
                is SearchScreenEvent.OnSearchQueryChanged -> Unit
            }

            viewModel.onEvent(event)
        },
    )
}

@Composable
fun SearchScreen(
    state: SearchScreenState,
    onAction: (SearchScreenEvent) -> Unit,
    modifier: Modifier,
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                onAction(SearchScreenEvent.OnBackPressed)
            }) {
                Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
            }

            NewsSearchField(
                modifier = Modifier.weight(1f),
                onAction = onAction,
                searchQuery = state.searchQuery,
            )
        }
        Spacer(modifier = Modifier.size(16.dp))

        Crossfade(
            targetState = state.isLoading
        ) { isLoading ->
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                if (state.label.isNotBlank()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = state.label,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = modifier,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(items = state.newsItems, key = { it.id }) { item ->
                            NewsItem(
                                item = item,
                                onClick = {
                                    onAction(SearchScreenEvent.OnNewsItemClicked(newsItem = item))
                                }
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun NewsSearchField(
    modifier: Modifier,
    onAction: (SearchScreenEvent) -> Unit,
    searchQuery: String,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { string ->
            onAction(SearchScreenEvent.OnSearchQueryChanged(string))
        },
        modifier = modifier,
        placeholder = {
            Text(text = stringResource(Res.string.search_placeholder), fontSize = 18.sp)
        },
        maxLines = 1,
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        shape = RoundedCornerShape(50),
        trailingIcon = {
            if (searchQuery.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    modifier = modifier.clickable(
                        enabled = true,
                        onClick = {
                            onAction(SearchScreenEvent.OnSearchQueryChanged(query = ""))
                        }
                    )
                )
            }
        }
    )
}