package com.alltrails.atlunch.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alltrails.atlunch.ui.discover.list.ListScreen
import com.alltrails.atlunch.ui.discover.map.MapScreen
import kotlinx.serialization.Serializable

@Serializable
object ListScreen

@Serializable
object MapScreen

@Composable
fun NavigationStack(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = ListScreen) {
        composable<ListScreen> {
            ListScreen(modifier = modifier)
        }
        composable<MapScreen> {
            MapScreen(modifier = modifier)
        }
    }
}
