package com.alltrails.atlunch.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchNearbyResponse(
    val places: List<Place>
)
