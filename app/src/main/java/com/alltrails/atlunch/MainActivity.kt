package com.alltrails.atlunch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alltrails.atlunch.navigation.ListScreen
import com.alltrails.atlunch.navigation.MapScreen
import com.alltrails.atlunch.navigation.NavigationStack
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
        enableEdgeToEdge()
        setContent {
            AtLunchTheme {
                val navController = rememberNavController()

                Scaffold(
                    topBar = { Header() },
                    floatingActionButton = { ToggleListMapFab(navController) },
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
                    NavigationStack(modifier = Modifier.padding(innerPadding), navController = navController)
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
fun Header() {
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
                query = TextFieldValue("Search restaurants"),
                onQueryChange = { },
                searchFocused = false,
                onSearchFocusChange = { },
                onClearQuery = { },
                modifier = Modifier.padding(all = 16.dp)
            )
            HorizontalDivider(color = Color(0xFFDBDAD2))
        }
    }
}

@Composable
private fun SearchBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    searchFocused: Boolean,
    onSearchFocusChange: (Boolean) -> Unit,
    onClearQuery: () -> Unit,
    modifier: Modifier = Modifier
) {
    var value by remember { mutableStateOf("") }

    Surface(
        color = Color(0xFFEFEFEC),
        contentColor = Color(0xFF656E5E),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .padding(horizontal = 20.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                modifier = Modifier.size(16.dp),
                contentDescription = null // decorative element
            )
            Spacer(Modifier.width(8.dp))
            BasicTextField(value = query,
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged {
                        onSearchFocusChange(it.isFocused)
                    })
        }
    }
}

@Composable
fun ToggleListMapFab(navController: NavController = rememberNavController()) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isMapScreen = currentBackStackEntry?.destination?.route == MapScreen::class.qualifiedName

    ExtendedFloatingActionButton(
        onClick = {
            if (isMapScreen) navController.navigate(ListScreen) else navController.navigate(MapScreen)
        },
        containerColor = MaterialTheme.colorScheme.primary,
        shape = MaterialTheme.shapes.small,
        icon = {
            Icon(
                painter = painterResource(id = if (isMapScreen) R.drawable.ic_list else R.drawable.ic_map_filled),
                contentDescription = null // decorative element
            )
        },
        text = { Text(text = stringResource(if (isMapScreen) R.string.list else R.string.map)) },
    )
}

@Preview(showSystemUi = true)
@Composable
fun ToggleListMapFabPreview() {
    AtLunchTheme {
        Scaffold(
            topBar = { Header() },
            floatingActionButton = { ToggleListMapFab() },
            floatingActionButtonPosition = FabPosition.Center,
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            NavigationStack(modifier = Modifier.padding(innerPadding))
        }
    }
}