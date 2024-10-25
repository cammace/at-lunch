package com.alltrails.atlunch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alltrails.atlunch.navigation.ListScreen
import com.alltrails.atlunch.navigation.MapScreen
import com.alltrails.atlunch.navigation.NavigationStack
import com.alltrails.atlunch.ui.components.SearchBar
import com.alltrails.atlunch.ui.components.ToggleListMapFab
import com.alltrails.atlunch.ui.discover.DiscoverViewModel
import com.alltrails.atlunch.ui.theme.AtLunchTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@OptIn(ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: DiscoverViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContent {
            AtLunchTheme {

                val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
                val navController = rememberNavController()

                val currentBackStackEntry by navController.currentBackStackEntryAsState()

                Scaffold(
                    topBar = {
                        Header(
                            searchQuery = searchQuery,
                            onSearchTriggered = { query ->
                                viewModel.onSearchTriggered(query)
                            },
                            onSearchFocusChange = {},
                            onSearchQueryChanged = { query ->
                                viewModel.onSearchQueryChanged(query)
                            },
                        )
                    },
                    floatingActionButton = {
                        ToggleListMapFab(
                            isToggledToMap = currentBackStackEntry?.destination?.route == MapScreen::class.qualifiedName,
                            onFabToggled = { isMapScreen ->
                                if (isMapScreen) navController.navigate(ListScreen) else navController.navigate(
                                    MapScreen
                                )
                            }
                        )
                    },
                    floatingActionButtonPosition = FabPosition.Center,
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val locationPermissionsState = rememberMultiplePermissionsState(
                        listOf(
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                        )
                    )

                    LocationPermissions(
                        locationPermissionsState = locationPermissionsState,
                        onPermissionGranted = { viewModel.onLocationPermissionGranted() }
                    )
                    NavigationStack(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LocationPermissions(
    locationPermissionsState: MultiplePermissionsState,
    onPermissionGranted: () -> Unit,
) {
    if (locationPermissionsState.permissions.any { it.status.isGranted }) {
        Timber.d("Location permissions granted from within LocationPermissions")
        onPermissionGranted()
    } else {
        Timber.d("Location permissions not granted. Requesting.")
        LaunchedEffect(locationPermissionsState) {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }
}

@Composable
fun Header(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_lockup),
                contentDescription = null, // decorative element
            )
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChanged = onSearchQueryChanged,
                onSearchTriggered = onSearchTriggered,
                onSearchFocusChange = onSearchFocusChange,
                modifier = modifier.padding(all = 16.dp)
            )
            HorizontalDivider(color = Color(0xFFDBDAD2))
        }
    }
}
