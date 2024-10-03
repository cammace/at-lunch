package com.alltrails.atlunch.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val id: String,
    val displayName: DisplayName? = null,
    val rating: Double? = null,
    val location: LatLng,
    val viewport: Viewport, // TODO: useful to center camera over place?
    val editorialSummary: EditorialSummary? = null,
    val formattedAddress: String,
    val googleMapsUri: String? = null,
    val websiteUri: String? = null,
    val businessStatus: String? = null,
    val priceLevel: String? = null,
    val userRatingCount: Int? = null,
    val iconMaskBaseUri: String? = null,
    val iconBackgroundColor: String? = null,
    val takeout: Boolean? = null,
    val delivery: Boolean? = null,
    val dineIn: Boolean? = null,
    val curbsidePickup: Boolean? = null,
    val reservable: Boolean? = null,
    val servesBreakfast: Boolean? = null,
    val servesLunch: Boolean? = null,
    val servesDinner: Boolean? = null,
    val servesBeer: Boolean? = null,
    val servesWine: Boolean? = null,
    val servesBrunch: Boolean? = null,
    val servesVegetarianFood: Boolean? = null,
)

@Serializable
data class Viewport(
    val low: LatLng,
    val high: LatLng
)

@Serializable
data class DisplayName(
    val text: String,
    val languageCode: String
)

@Serializable
data class EditorialSummary(
    val text: String,
    val languageCode: String
)
