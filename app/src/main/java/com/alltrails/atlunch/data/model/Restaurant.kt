package com.alltrails.atlunch.data.model

import com.alltrails.atlunch.data.network.model.Place
import com.google.android.gms.maps.model.LatLng

data class Restaurant(
    val id: String,
    val displayName: String,
    val rating: Double,
    val address: String,
    val userRatingCount: Int,
    val latLng: LatLng,
)

fun Place.asExternalModel() = Restaurant(
    id = id,
    displayName = displayName?.text ?: "",
    rating = rating ?: 0.0,
    address = formattedAddress,
    userRatingCount = userRatingCount ?: 0,
    latLng = LatLng(location.latitude, location.longitude)
)
