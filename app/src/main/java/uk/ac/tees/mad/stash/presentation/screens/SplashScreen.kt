package uk.ac.tees.mad.stash.presentation.screens

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import kotlinx.coroutines.flow.first
import uk.ac.tees.mad.stash.navigation.NavRoutes
import uk.ac.tees.mad.stash.presentation.ViewModel.AppViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: AppViewModel
) {
    LaunchedEffect(Unit) {
        delay(1000)
        
        // Get biometric preference value directly from DataStore (suspend)
        // This avoids the race condition where StateFlow.value is false (initial)
        val biometricEnabled = viewModel.getBiometricEnabledSuspend()
        
        if (viewModel.isUserLoggedIn) {
            if (biometricEnabled) {
                // Navigate to SecureUnlock if biometric is enabled
                navController.navigate(NavRoutes.SECURE_UNLOCK) {
                    popUpTo(NavRoutes.SPLASH) { inclusive = true }
                }
            } else {
                // Navigate directly to Home if biometric is disabled
                navController.navigate(NavRoutes.HOME) {
                    popUpTo(NavRoutes.SPLASH) { inclusive = true }
                }
            }
        } else {
            // Navigate to Login if not logged in
            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(NavRoutes.SPLASH) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = uk.ac.tees.mad.stash.R.drawable.stashlogo),
            contentDescription = "App Logo"
        )
    }
}
