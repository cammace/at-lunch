package com.alltrails.atlunch.data

import com.alltrails.atlunch.data.model.Restaurant
import com.alltrails.atlunch.data.model.asExternalModel
import com.alltrails.atlunch.data.network.PlacesRemoteDataSource
import com.alltrails.atlunch.data.network.model.Circle
import com.alltrails.atlunch.data.network.model.LatLng
import com.alltrails.atlunch.data.network.model.LocationRestriction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepository @Inject constructor(
    private val network: PlacesRemoteDataSource
) {

    private val _restaurants = MutableStateFlow<Result<List<Restaurant>>>(Result.success(emptyList()))
    val restaurants: StateFlow<Result<List<Restaurant>>> = _restaurants.asStateFlow()

    suspend fun getNearbyRestaurants(
        center: LatLng,
        radius: Int
    ) {
        // TODO: add caching. Simply get the data from network everytime for now.
        try {
            val response = network.getNearbyRestaurants(LocationRestriction(Circle(center, radius)))
            _restaurants.value = Result.success(response.places.map { it.asExternalModel() })
        } catch (e: Exception) {
            Timber.e("Error getting nearby restaurants", e)
            _restaurants.value = Result.failure(e)
        }
    }

    // TODO add debouncing to prevent rapid API calls.
    suspend fun searchText(query: String) = network.searchText(query)
}
