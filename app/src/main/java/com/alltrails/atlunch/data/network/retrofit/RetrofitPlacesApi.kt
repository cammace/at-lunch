package com.alltrails.atlunch.data.network.retrofit

import com.alltrails.atlunch.BuildConfig
import com.alltrails.atlunch.data.network.PlacesRemoteDataSource
import com.alltrails.atlunch.data.network.model.LocationRestriction
import com.alltrails.atlunch.data.network.model.SearchNearbyRequest
import com.alltrails.atlunch.data.network.model.SearchNearbyResponse
import com.alltrails.atlunch.data.network.model.SearchTextRequest
import com.alltrails.atlunch.data.network.model.SearchTextResponse
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit API declaration for Places JSON API.
 */
private interface RetrofitPlacesApi {

    @POST("v1/places:searchNearby?fields=*")
    suspend fun searchNearbyPlaces(
        @Query("key") accessToken: String,
        @Body requestBody: SearchNearbyRequest
    ): SearchNearbyResponse

    @POST("v1/places:searchText?fields=*")
    suspend fun searchText(
        @Query("key") accessToken: String,
        @Body requestBody: SearchTextRequest
    ): SearchTextResponse
}

private const val BASE_URL = "https://places.googleapis.com/"

/**
 * [Retrofit] backed [PlacesRemoteDataSource]
 */
@Singleton
class RetrofitPlacesRemoteDataSource @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
) : PlacesRemoteDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(RetrofitPlacesApi::class.java)

    override suspend fun getNearbyRestaurants(
        locationRestriction: LocationRestriction
    ): SearchNearbyResponse {
        val requestBody = SearchNearbyRequest(
            includedTypes = listOf("restaurant"),
            maxResultCount = 20,
            locationRestriction = locationRestriction
        )
        return networkApi.searchNearbyPlaces(accessToken = BuildConfig.MAPS_API_KEY, requestBody = requestBody)
    }

    override suspend fun searchText(query: String): SearchTextResponse {
        // Create the request body
        val requestBody = SearchTextRequest(
            textQuery = query,
            maxResultCount = 10,
            strictTypeFiltering = true,
            includedType = "restaurant"
        )

        return networkApi.searchText(accessToken = BuildConfig.MAPS_API_KEY, requestBody = requestBody)
    }
}
