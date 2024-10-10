package com.alltrails.atlunch.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseInOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alltrails.atlunch.ui.discover.ListScreen
import com.alltrails.atlunch.ui.discover.MapScreen
import kotlinx.serialization.Serializable

@Serializable
object ListScreen

@Serializable
object MapScreen

private const val TRANSITION_ANIMATION_DURATION_MILLIS = 450

@Composable
fun NavigationStack(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = ListScreen) {
        composable<ListScreen>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(TRANSITION_ANIMATION_DURATION_MILLIS, easing = EaseInOutQuart)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(TRANSITION_ANIMATION_DURATION_MILLIS, easing = EaseInOutQuart)
                )
            }
        ) {
            ListScreen(modifier = modifier)
        }
        composable<MapScreen>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(TRANSITION_ANIMATION_DURATION_MILLIS, easing = EaseInOutQuart)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(TRANSITION_ANIMATION_DURATION_MILLIS, easing = EaseInOutQuart)
                )
            }
        ) {
            MapScreen(modifier = modifier)
        }
    }
}
