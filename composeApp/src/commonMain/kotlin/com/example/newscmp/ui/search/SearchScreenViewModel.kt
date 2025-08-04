package com.example.newscmp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newscmp.core.network.Error
import com.example.newscmp.core.network.Result
import com.example.newscmp.domain.repository.NewsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchScreenViewModel(
    private val newsRepository: NewsRepository
): ViewModel() {

    private val _state = MutableStateFlow(SearchScreenState())
    val state = _state
        .onStart {
            observeSearchQuery()
        }.stateIn(viewModelScope, SharingStarted.Lazily, SearchScreenState())

    private var newsFetchJob: Job? = null

    fun observeSearchQuery() {
        _state.map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(500)
            .onEach { query ->
                if (query.isBlank() || query.length < 3) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            label = "Stay updated with latest news...",
                        )
                    }
                } else {
                    fetchNewsItems(query)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun fetchNewsItems(query: String) {
        _state.update {
            it.copy(
                isLoading = true,
            )
        }

        newsFetchJob?.cancel()
        newsFetchJob = viewModelScope.launch {
            val result = newsRepository.getNewsItems(query = query, cache = false)
            when(result) {
                is Result.Failure -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            label = result.error::class.simpleName ?: Error.NetworkError.UNKNOWN.name,
                            newsItems = emptyList(),
                        )
                    }
                }
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            label = "",
                            newsItems = result.data
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: SearchScreenEvent) {
       when(event) {
           SearchScreenEvent.OnBackPressed -> Unit
           is SearchScreenEvent.OnNewsItemClicked -> Unit
           is SearchScreenEvent.OnSearchQueryChanged -> {
               _state.update {
                   it.copy(searchQuery = event.query)
               }
           }
       }
    }
}