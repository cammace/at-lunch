package com.alltrails.atlunch.ui.discover

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alltrails.atlunch.R
import com.alltrails.atlunch.data.model.Restaurant
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: DiscoverViewModel = hiltViewModel()
) {

    val state by viewModel.discoveryUiState.collectAsStateWithLifecycle()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(42.35728, -71.05232), 14f)
    }
    val scope = rememberCoroutineScope()

    when (val uiState = state) {
        is DiscoveryUiState.Loading -> Unit // TODO show a loading UI
        is DiscoveryUiState.LoadFailed -> Unit // TODO show an error state
        is DiscoveryUiState.NoResults -> Unit // TODO show an empty state
        is DiscoveryUiState.Success -> {
            Box(
                modifier = Modifier
            ) {
                AtLunchMap(
                    modifier = Modifier,
                    cameraPositionState = cameraPositionState,
                    onMapLoaded = {
                        scope.launch {
                            updateCameraBounds(uiState.restaurants, cameraPositionState)
                        }
                    }
                ) {
                    AddMarkers(uiState.restaurants)
                }
            }
        }
    }
}

@Composable
private fun AddMarkers(state: List<Restaurant>, modifier: Modifier = Modifier) {
    state.forEach { restaurant ->
        val markerState = rememberMarkerState(
            position = restaurant.latLng,
            key = restaurant.displayName
        )
        RestaurantMarker(modifier = modifier, markerState = markerState, restaurant = restaurant)
    }
}

private fun updateCameraBounds(
    restaurants: List<Restaurant>,
    cameraPositionState: CameraPositionState
) {
    // Avoid creating empty bounds when no restaurants are loaded.
    if (restaurants.isEmpty()) return

    val markerBoundsBuilder = LatLngBounds.builder()
    restaurants.forEach { restaurant ->
        markerBoundsBuilder.include(restaurant.latLng)
    }
    val markerBounds = markerBoundsBuilder.build()
    cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(markerBounds, 16))
}

@Composable
fun RestaurantMarker(
    modifier: Modifier = Modifier,
    markerState: MarkerState = rememberMarkerState(),
    restaurant: Restaurant,
    markerClick: (Marker) -> Boolean = { false }
) {
    MarkerComposable(
        title = restaurant.displayName, // TODO disable opening default info window in favor of list card UI
//        keys = , TODO animate icon changing when the user clicks on the marker.
        state = markerState,
        onClick = markerClick,
        contentDescription = stringResource(R.string.cd_restaurant_marker, restaurant.displayName)
    ) {
        Image(
            painter = painterResource(id = R.drawable.marker_pin_resting),
            modifier = Modifier
                .width(30.dp)
                .height(39.dp),
            contentDescription = null // Parent MarkerComposed has a contentDescription.
        )
    }
}

@Composable
fun AtLunchMap(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapLoaded: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                compassEnabled = false,
                indoorLevelPickerEnabled = false,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = false,
                rotationGesturesEnabled = false,
                tiltGesturesEnabled = false,
                zoomControlsEnabled = false
            )
        )
    }
    val properties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }

    GoogleMap(
        modifier = modifier,
        properties = properties,
        cameraPositionState = cameraPositionState,
        mapColorScheme = ComposeMapColorScheme.LIGHT,
        onMapLoaded = onMapLoaded,
        uiSettings = uiSettings,
        locationSource = null,
    ) {
        content()
    }
}
