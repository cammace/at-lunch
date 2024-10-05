package com.alltrails.atlunch.ui.discover

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alltrails.atlunch.data.PlacesRepository
import com.alltrails.atlunch.data.model.Restaurant
import com.alltrails.atlunch.data.network.model.LatLng
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

// Used when the user doesn't grant location permissions.
private val defaultLocation = LatLng(42.35728, -71.05232)

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle, // TODO stash and restore search query state.
    private val locationClient: FusedLocationProviderClient,
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val _locationGranted = MutableStateFlow(false)
    val locationGranted: StateFlow<Boolean> = _locationGranted.asStateFlow()

    val restaurants: StateFlow<Result<List<Restaurant>>> = placesRepository.restaurants

    fun onLocationPermissionGranted() {
        Timber.d("Location permission granted")
        _locationGranted.value = true
        getNearbyRestaurants()
    }

    private fun getNearbyRestaurants() {
        Timber.d("Getting nearby restaurants")
        viewModelScope.launch {
            val userLatLng = getLocation()
            Timber.d("User location: $userLatLng")
            placesRepository.getNearbyRestaurants(center = userLatLng ?: defaultLocation, radius = 5000)
        }
    }

    @SuppressLint("MissingPermission") // Location permission is checked
    suspend fun getLocation(): LatLng? {
        return try {
            if (locationGranted.value) {
                val location: Location = locationClient.lastLocation.await()
                LatLng(location.latitude, location.longitude)
            } else {
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "Error getting location")
            null
        }
    }
}
