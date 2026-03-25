package uk.ac.tees.mad.stash.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uk.ac.tees.mad.stash.AppViewModelFactory
import uk.ac.tees.mad.stash.presentation.ViewModel.AppViewModel
import uk.ac.tees.mad.stash.presentation.screens.HomeScreen

import uk.ac.tees.mad.stash.presentation.screens.LoginScreen
import uk.ac.tees.mad.stash.presentation.screens.RecordScreen
import uk.ac.tees.mad.stash.presentation.screens.SignupScreen

@Composable
fun StashNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(route = NavRoutes.LOGIN) { backStackEntry ->

            val viewModel: AppViewModel = viewModel(
                factory = AppViewModelFactory(backStackEntry)
            )

            LoginScreen(
                viewModel = viewModel,
                navController = navController
            )
        }


        composable(route = NavRoutes.SIGNUP) { backStackEntry ->

            val viewModel: AppViewModel = viewModel(
                factory = AppViewModelFactory(backStackEntry)
            )

            SignupScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(route = NavRoutes.HOME) { backStackEntry ->

            val viewModel: AppViewModel = viewModel(
                factory = AppViewModelFactory(backStackEntry)
            )

            // Fetch records when entering home
            LaunchedEffect(Unit) {
                viewModel.getAllRecords()
            }

            HomeScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable (route = NavRoutes.EDIT_RECORD){
            backStackEntry ->
            val viewModel: AppViewModel=viewModel (
                factory = AppViewModelFactory(backStackEntry )
            )


        }
        composable(
            route = NavRoutes.RECORD,
            arguments = listOf(
                navArgument("recordId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->

            val viewModel: AppViewModel = viewModel(
                factory = AppViewModelFactory(backStackEntry)
            )

            val recordId = backStackEntry.arguments?.getString("recordId")

            RecordScreen(
                recordId = recordId,
                viewModel = viewModel,
                navController = navController
            )
        }

    }
}
