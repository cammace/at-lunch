package com.alltrails.atlunch.ui.discover.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alltrails.atlunch.ui.theme.AtLunchTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen(
    modifier: Modifier = Modifier
) {
    val singapore = LatLng(1.35, 103.87)
    val singaporeMarkerState = rememberMarkerState(position = singapore)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }


    Map(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    )
}

@Composable
fun Map(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapLoaded: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(),
        cameraPositionState = cameraPositionState,
        mapColorScheme = ComposeMapColorScheme.LIGHT,
        onMapLoaded = onMapLoaded
    ) {
//        Marker(
//            state = singaporeMarkerState,
//            title = "Singapore",
//            snippet = "Marker in Singapore"
//        )
    }
}

@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    AtLunchTheme {
        Map(Modifier.fillMaxSize())
    }
}