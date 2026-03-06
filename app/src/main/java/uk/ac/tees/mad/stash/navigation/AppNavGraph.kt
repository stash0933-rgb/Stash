package uk.ac.tees.mad.stash.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uk.ac.tees.mad.stash.AppViewModelFactory
import uk.ac.tees.mad.stash.presentation.ViewModel.AppViewModel
import uk.ac.tees.mad.stash.presentation.screens.LoginScreen

@Composable
fun StashNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.LOGIN
    ) {

        composable(
            route = NavRoutes.LOGIN_WITH_ARG,
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->

            val viewModel: AppViewModel = viewModel(
                factory = AppViewModelFactory(backStackEntry)
            )

            LoginScreen()
        }
    }
}
