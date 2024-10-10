package com.alltrails.atlunch.ui.discover

import com.alltrails.atlunch.data.model.Restaurant

sealed interface DiscoveryUiState {
    data object Loading : DiscoveryUiState

    data object LoadFailed : DiscoveryUiState

    data object NoResults : DiscoveryUiState

    /**
     * The search results and nearby restaurants are rendered the same way
     * therefore the UI doesn't need to know where the data came from.
     */
    data class Success(
        val restaurants: List<Restaurant> = emptyList(),
    ) : DiscoveryUiState {
        fun hasRestaurants(): Boolean = restaurants.isNotEmpty()
    }
}
