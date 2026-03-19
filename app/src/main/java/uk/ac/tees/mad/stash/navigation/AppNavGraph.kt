package uk.ac.tees.mad.stash.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.stash.AppViewModelFactory
import uk.ac.tees.mad.stash.presentation.ViewModel.AppViewModel

import uk.ac.tees.mad.stash.presentation.screens.LoginScreen
import uk.ac.tees.mad.stash.presentation.screens.SignupScreen

@Composable
fun StashNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.LOGIN
    ) {

        // ✅ LOGIN SCREEN
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

//            HomeScreen(
//                viewModel = viewModel,
//                navController = navController
//            )
        }
    }
}
