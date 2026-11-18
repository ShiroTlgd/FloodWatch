package br.com.fiap.rm98825.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.fiap.rm98825.data.local.UserPreferences
import br.com.fiap.rm98825.ui.screens.AreaFormScreen
import br.com.fiap.rm98825.ui.screens.DetailsScreen
import br.com.fiap.rm98825.ui.screens.FavoritesScreen
import br.com.fiap.rm98825.ui.screens.HomeScreen
import br.com.fiap.rm98825.ui.screens.IntroScreen
import br.com.fiap.rm98825.ui.screens.SplashScreen
import br.com.fiap.rm98825.ui.screens.WeatherScreen
import br.com.fiap.rm98825.viewmodel.AreaViewModel
import br.com.fiap.rm98825.viewmodel.WeatherViewModel

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val preferences = remember { UserPreferences(context) }
    val areaViewModel: AreaViewModel = viewModel()
    val weatherViewModel: WeatherViewModel = viewModel()
    val nav = rememberNavController()

    val idArgument = listOf(navArgument(Routes.ARG_ID) { type = NavType.IntType })

    NavHost(navController = nav, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    val next = if (preferences.introSeen) Routes.HOME else Routes.INTRO
                    nav.navigate(next) { popUpTo(Routes.SPLASH) { inclusive = true } }
                }
            )
        }
        composable(Routes.INTRO) {
            IntroScreen(
                onFinish = { dontShowAgain ->
                    if (dontShowAgain) preferences.introSeen = true
                    nav.navigate(Routes.HOME) { popUpTo(Routes.INTRO) { inclusive = true } }
                }
            )
        }
        composable(Routes.HOME) { HomeScreen(areaViewModel, nav) }
        composable(Routes.FAVORITES) { FavoritesScreen(areaViewModel, nav) }
        composable(Routes.WEATHER) { WeatherScreen(areaViewModel, weatherViewModel, nav) }
        composable(Routes.FORM) { AreaFormScreen(areaViewModel, nav, areaId = null) }
        composable(Routes.DETAILS, arguments = idArgument) { entry ->
            DetailsScreen(
                areaViewModel,
                weatherViewModel,
                nav,
                areaId = entry.arguments?.getInt(Routes.ARG_ID) ?: 0
            )
        }
        composable(Routes.EDIT, arguments = idArgument) { entry ->
            AreaFormScreen(
                areaViewModel,
                nav,
                areaId = entry.arguments?.getInt(Routes.ARG_ID)
            )
        }
    }
}
