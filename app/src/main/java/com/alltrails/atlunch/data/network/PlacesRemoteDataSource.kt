package com.alltrails.atlunch.data.network

import com.alltrails.atlunch.data.network.model.LocationRestriction
import com.alltrails.atlunch.data.network.model.SearchNearbyResponse
import com.alltrails.atlunch.data.network.model.SearchTextResponse

/**
 * Interface representing network calls to the Google Places API.
 */
interface PlacesRemoteDataSource {
    suspend fun getNearbyRestaurants(locationRestriction: LocationRestriction): SearchNearbyResponse
    suspend fun searchText(query: String, locationRestriction: LocationRestriction): SearchTextResponse
}
