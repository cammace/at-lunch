package com.alltrails.atlunch.ui.discover.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alltrails.atlunch.R
import com.alltrails.atlunch.data.model.Restaurant
import com.alltrails.atlunch.ui.discover.DiscoverViewModel
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
    val state by viewModel.restaurants.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(42.35728, -71.05232), 14f)
    }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
    ) {
        AtLunchMap(
            modifier = Modifier,
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                scope.launch {
                    updateCameraBounds(state, cameraPositionState)
                }
            }
        ) {
            state.getOrNull()?.let { restaurants ->
                AddMarkers(restaurants)
            }
        }
    }
}

@Composable
private fun AddMarkers(state: List<Restaurant>, modifier: Modifier = Modifier) {
    state.forEach { restaurant ->
        val markerState = rememberMarkerState(
            position = restaurant.latLng,
            key = restaurant.name
        ) // TODO change restaurant so it includes id/name and rename current name variable to displayName
        RestaurantMarker(modifier = modifier, markerState = markerState, restaurant = restaurant)
    }
}

private suspend fun updateCameraBounds(
    state: Result<List<Restaurant>>,
    cameraPositionState: CameraPositionState
) {
    state.getOrNull()?.let { restaurants ->
        val markerBoundsBuilder = LatLngBounds.builder()
        restaurants.forEach { restaurant ->
            markerBoundsBuilder.include(restaurant.latLng)
        }
        val markerBounds = markerBoundsBuilder.build()
        cameraPositionState.animate(CameraUpdateFactory.newLatLngBounds(markerBounds, 16), 1000)
    }
}

@Composable
fun RestaurantMarker(
    modifier: Modifier = Modifier,
    markerState: MarkerState = rememberMarkerState(),
    restaurant: Restaurant,
    markerClick: (Marker) -> Boolean = { false }
) {
    MarkerComposable(
        title = restaurant.name, // TODO disable opening default infowindow in favor of ist card UI
//        keys = , TODO animate icon changing when the user clicks on the marker.
        state = markerState,
        onClick = markerClick,
        contentDescription = stringResource(R.string.cd_restaurant_marker, restaurant.name)
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
    val properties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
//                isMyLocationEnabled = true,
                latLngBoundsForCameraTarget = null // TODO provide initial camera position
            )
        )
    }

    GoogleMap(
        modifier = modifier,
        properties = properties,
        cameraPositionState = cameraPositionState,
        mapColorScheme = ComposeMapColorScheme.LIGHT,
        onMapLoaded = onMapLoaded,
        uiSettings = uiSettings,
        locationSource = null, // TODO setup location source
    ) {
        content()
    }
}
