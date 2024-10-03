package com.alltrails.atlunch.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alltrails.atlunch.data.PlacesRepository
import com.alltrails.atlunch.data.model.Restaurant
import com.alltrails.atlunch.data.network.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    placesRepository: PlacesRepository
) : ViewModel() {

    val restaurants: StateFlow<Result<List<Restaurant>>> = placesRepository.restaurants

    init {
        // TODO: fetch restaurants using device current location.
        // Trigger the API call
        viewModelScope.launch {
            placesRepository.getNearbyRestaurants(center = LatLng(37.7807705, -122.4339193), radius = 500)
        }
    }

}
