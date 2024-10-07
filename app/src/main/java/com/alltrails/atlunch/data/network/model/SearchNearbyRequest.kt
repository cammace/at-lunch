package com.alltrails.atlunch.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchNearbyRequest(
    val includedTypes: List<String>,
    val maxResultCount: Int,
    val rankPreference: RankPreference = RankPreference.DISTANCE,
    val locationRestriction: LocationRestriction
)

/**
 * The type of ranking to use. If this parameter is omitted, results are
 * ranked by popularity.
 */
@Serializable
enum class RankPreference {
    /**
     * (default) Sorts results based on their popularity.
     */
    POPULARITY,

    /**
     * Sorts results in ascending order by their distance from the specified
     * location.
     */
    DISTANCE
}

@Serializable
data class LocationRestriction(
    val circle: Circle
)

@Serializable
data class Circle(
    val center: LatLng,
    val radius: Int
)

@Serializable
data class LatLng(
    val latitude: Double,
    val longitude: Double
)
