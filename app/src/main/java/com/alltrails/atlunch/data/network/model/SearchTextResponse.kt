package com.alltrails.atlunch.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchTextResponse(
    // Define the structure of the response for searchText here
    val places: List<Place>
)