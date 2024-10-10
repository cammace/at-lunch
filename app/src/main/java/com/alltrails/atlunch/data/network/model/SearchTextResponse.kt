package com.alltrails.atlunch.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchTextResponse(
    val places: List<Place>? = emptyList()
)
