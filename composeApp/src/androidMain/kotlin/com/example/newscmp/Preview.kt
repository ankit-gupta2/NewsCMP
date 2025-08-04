package com.example.newscmp

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.newscmp.ui.home.HomeScreen
import com.example.newscmp.ui.home.HomeScreenState

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(state = HomeScreenState(), modifier = Modifier.statusBarsPadding()) { }
}