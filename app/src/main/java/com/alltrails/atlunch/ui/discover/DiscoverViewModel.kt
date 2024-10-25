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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject


/**
 * Minimum length that the search query is considered as
 * [DiscoveryUiState.EmptyQuery].
 */
private const val SEARCH_QUERY_MIN_LENGTH = 2

private const val KEY_SEARCH_QUERY = "com.alltrails.atlunch.keys.searchQuery"

/**
 * Used when the user doesn't grant location permissions.
 */
private val DEFAULT_LOCATION = LatLng(42.35728, -71.05232)

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val locationClient: FusedLocationProviderClient,
    private val placesRepository: PlacesRepository
) : ViewModel() {

    val searchQuery = savedStateHandle.getStateFlow(key = KEY_SEARCH_QUERY, initialValue = "")

    private val _locationGranted = MutableStateFlow(false)
    val locationGranted: StateFlow<Boolean> = _locationGranted.asStateFlow()

    val discoveryUiState: StateFlow<DiscoveryUiState> = searchQuery.flatMapLatest { query ->
        Timber.d("discoveryUiState invoked with query: $query")
        if (query.length < SEARCH_QUERY_MIN_LENGTH) {
            getNearbyRestaurants().map { handleData(it) }.stateIn(viewModelScope)
        } else {
            searchText(query).map { handleData(it) }.stateIn(viewModelScope)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
//        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DiscoveryUiState.Loading,
    )

    private fun handleData(data: Result<List<Restaurant>>): DiscoveryUiState =
        if (data.isSuccess) {
            if (data.getOrNull().isNullOrEmpty()) {
                DiscoveryUiState.NoResults
            } else {
                DiscoveryUiState.Success(data.getOrDefault(emptyList()))
            }
        } else {
            DiscoveryUiState.LoadFailed
        }


    fun onLocationPermissionGranted() {
        Timber.d("Location permission granted")
        _locationGranted.value = true
    }

    private suspend fun searchText(query: String): Flow<Result<List<Restaurant>>> {
        Timber.d("Getting search text")
        val userLatLng = getLocation()
        return placesRepository.searchText(query, userLatLng, 5000)
    }


    private suspend fun getNearbyRestaurants(): Flow<Result<List<Restaurant>>> {
        Timber.d("Getting nearby restaurants")
        val userLatLng = getLocation()
        return placesRepository.getNearbyRestaurants(center = userLatLng, radius = 5000)
    }

    @SuppressLint("MissingPermission") // Location permission is checked
    @Throws()
    suspend fun getLocation(): LatLng {
        return try {
            if (locationGranted.value) {
                val location: Location = locationClient.lastLocation.await()
                val userLatLng = LatLng(location.latitude, location.longitude)
                Timber.d("User location: $userLatLng")
                userLatLng
            } else {
                Timber.e("Error getting location, location permission not granted.")
                DEFAULT_LOCATION
            }
        } catch (e: Exception) {
            Timber.e(e, "Error getting location, falling back to the default location.")
            DEFAULT_LOCATION
        }
    }

    fun onSearchQueryChanged(query: String) {
        Timber.d("Search query changed: $query")
        savedStateHandle[KEY_SEARCH_QUERY] = query
    }

    /**
     * Called when the search action is explicitly triggered by the user. For
     * example, when the search icon is tapped in the IME or when the enter key
     * is pressed in the search text field.
     *
     * The results **are not** being displayed on the fly as the user types,
     * for that we'd need to implement Google Places Autocomplete API.
     */
    fun onSearchTriggered(searchQuery: String) {
        viewModelScope.launch {
            val userLatLng = getLocation()
            placesRepository.searchText(query = searchQuery, center = userLatLng, radius = 5000)
        }
    }
}
