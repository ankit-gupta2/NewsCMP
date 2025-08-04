package com.example.newscmp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newscmp.core.network.Error
import com.example.newscmp.core.network.Result
import com.example.newscmp.domain.model.NewsCategory
import com.example.newscmp.domain.repository.NewsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val newsRepository: NewsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState())
    val state = _state
        .onStart {
            fetchNewsItems(category = _state.value.selectedNewsCategory)
        }.stateIn(viewModelScope, SharingStarted.Lazily, HomeScreenState())

    private var newsFetchJob: Job? = null

//    fun observeSearchQuery() {
//        state.map { it.searchQuery }
//            .distinctUntilChanged()
//            .debounce(500)
//            .onEach { query ->
//               if (query.isBlank()) {
//                   _state.update {
//                       it.copy(
//                           isLoading = false,
//                       )
//                   }
//               } else {
//                   _state.update {
//                       it.copy(
//                           isLoading = true,
//                           canFetchMoreItems = false,
//                       )
//                   }
//
//               }
//            }.launchIn(viewModelScope)
//    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnNewsCategoryChanged -> {
                _state.update {
                    it.copy(selectedNewsCategory = event.newsCategory)
                }
                fetchNewsItems(category = event.newsCategory)
            }

            is HomeScreenEvent.OnNewsItemClicked -> Unit
            HomeScreenEvent.OnSearchButtonClicked -> Unit
        }
    }

    fun fetchNewsItems(category: NewsCategory) {
        _state.update {
            it.copy(
                isLoading = true,
                errorLabel = "",
            )
        }
        newsFetchJob?.cancel()
        newsFetchJob = viewModelScope.launch {
            val result = newsRepository.getNewsItems(query = category.name, cache = true)
            when(result) {
                is Result.Failure -> _state.update {
                    it.copy(
                        isLoading = false,
                        errorLabel = result.error::class.simpleName ?: Error.NetworkError.UNKNOWN.name
                    )
                }
                is Result.Success -> _state.update {
                    it.copy(
                        isLoading = false,
                        errorLabel = "",
                        newsItems = result.data
                    )
                }
            }
        }
    }

}