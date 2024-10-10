package com.alltrails.atlunch.data

import com.alltrails.atlunch.data.model.Restaurant
import com.alltrails.atlunch.data.model.asExternalModel
import com.alltrails.atlunch.data.network.PlacesRemoteDataSource
import com.alltrails.atlunch.data.network.model.Circle
import com.alltrails.atlunch.data.network.model.LatLng
import com.alltrails.atlunch.data.network.model.LocationRestriction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepository @Inject constructor(
    private val network: PlacesRemoteDataSource
) {

    fun getNearbyRestaurants(
        center: LatLng,
        radius: Int
    ): Flow<Result<List<Restaurant>>> = flow {
        // TODO: add caching. Simply getting the data from network everytime for now.
        try {
            val response = network.getNearbyRestaurants(LocationRestriction(Circle(center, radius)))
            emit(Result.success(response.places?.map { it.asExternalModel() } ?: emptyList()))
        } catch (e: Exception) {
            Timber.e(e, "Error getting nearby restaurants")
            emit(Result.failure(e))
        }
    }

    // TODO add debouncing to prevent rapid API calls.
    fun searchText(
        query: String,
        center: LatLng,
        radius: Int
    ): Flow<Result<List<Restaurant>>> = flow {
        try {
            val response = network.searchText(query, LocationRestriction(Circle(center, radius)))
            emit(Result.success(response.places?.map { it.asExternalModel() } ?: emptyList()))
        } catch (e: Exception) {
            Timber.e(e, "Error searching for the restaurant using the \"$query\".")
            emit(Result.failure(e))
        }
    }
}
