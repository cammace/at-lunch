package com.alltrails.atlunch.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchTextRequest(
    val textQuery: String,
    val maxResultCount: Int,
    val strictTypeFiltering: Boolean,
    val includedType: String,
    val locationBias: LocationRestriction
)
