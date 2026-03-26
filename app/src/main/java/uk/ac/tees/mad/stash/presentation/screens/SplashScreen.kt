package uk.ac.tees.mad.stash.presentation.screens

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import uk.ac.tees.mad.stash.navigation.NavRoutes
import uk.ac.tees.mad.stash.presentation.ViewModel.AppViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: AppViewModel
) {
    LaunchedEffect(Unit) {
        delay(1000) // Optional: Give user a moment to see splash
        if (viewModel.isUserLoggedIn) {
            navController.navigate(NavRoutes.HOME) {
                popUpTo(NavRoutes.SPLASH) { inclusive = true }
            }
        } else {
            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(NavRoutes.SPLASH) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = uk.ac.tees.mad.stash.R.mipmap.ic_launcher),
            contentDescription = "App Logo"
        )
    }
}
