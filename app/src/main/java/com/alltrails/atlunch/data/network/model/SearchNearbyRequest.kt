package com.alltrails.atlunch.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchNearbyRequest(
    val includedTypes: List<String>,
    val maxResultCount: Int,
    val locationRestriction: LocationRestriction
)

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
