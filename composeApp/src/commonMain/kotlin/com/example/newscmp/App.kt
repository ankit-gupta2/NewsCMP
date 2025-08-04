package com.example.newscmp

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.newscmp.core.ui.Screen
import com.example.newscmp.ui.SharedNewsViewModel
import com.example.newscmp.ui.home.HomeScreenRoot
import com.example.newscmp.ui.news.NewsScreenRoot
import com.example.newscmp.ui.search.SearchScreenRoot
import com.example.newscmp.ui.search.SearchScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        AppNavHost()
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.NewsGraph) {
        navigation<Screen.NewsGraph>(startDestination = Screen.Home) {
            composable<Screen.Home> {
                val sharedNewsViewModel = it.sharedViewModel<SharedNewsViewModel>(navController)
                HomeScreenRoot(
                    homeScreenViewModel = koinViewModel(),
                    onNewsItemClicked = { item ->
                        sharedNewsViewModel.updateNewsItem(newsItem = item)
                        navController.navigate(Screen.News)
                    },
                    onSearchButtonClicked = {
                        navController.navigate(Screen.Search)
                    },
                    modifier = Modifier.statusBarsPadding(),
                )
            }

            composable<Screen.Search> {
                val sharedNewsViewModel = it.sharedViewModel<SharedNewsViewModel>(navController)
                SearchScreenRoot(
                    viewModel = koinViewModel<SearchScreenViewModel>(),
                    onBackPressed = {
                        navController.navigateUp()
                    },
                    onNewsItemClicked =  {
                        sharedNewsViewModel.updateNewsItem(newsItem = it)
                        navController.navigate(Screen.News)
                    },
                    modifier = Modifier.statusBarsPadding()
                )
            }

            composable<Screen.News> {
                val sharedNewsViewModel = it.sharedViewModel<SharedNewsViewModel>(navController)
                NewsScreenRoot(
                    viewModel = sharedNewsViewModel,
                    modifier = Modifier.statusBarsPadding(),
                    onBackPressed = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

@Composable
inline fun <reified T: ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController
): T {
    val parent = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(parent)
    }

    return koinViewModel<T>(viewModelStoreOwner = parentEntry)
}