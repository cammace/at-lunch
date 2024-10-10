package com.alltrails.atlunch.ui.discover

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alltrails.atlunch.ui.components.RestaurantCard

@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    viewModel: DiscoverViewModel = hiltViewModel()
) {
    val state by viewModel.discoveryUiState.collectAsStateWithLifecycle()

    when (val uiState = state) {
        is DiscoveryUiState.Loading -> Unit // TODO show a loading UI
        is DiscoveryUiState.LoadFailed -> Unit // TODO show an error state
        is DiscoveryUiState.NoResults -> Unit // TODO show an empty state
        is DiscoveryUiState.Success -> {
            LazyColumn(modifier = modifier, contentPadding = PaddingValues(vertical = 8.dp)) {
                items(uiState.restaurants) {
                    RestaurantCard(restaurant = it)
                }
            }
        }
    }
}

