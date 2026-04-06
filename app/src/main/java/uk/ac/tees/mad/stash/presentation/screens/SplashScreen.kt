package uk.ac.tees.mad.stash.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.stash.R
import uk.ac.tees.mad.stash.navigation.NavRoutes
import uk.ac.tees.mad.stash.presentation.ViewModel.AppViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: AppViewModel
) {

    LaunchedEffect(Unit) {

        delay(1000)

        val biometricEnabled = viewModel.getBiometricEnabledSuspend()

        if (viewModel.isUserLoggedIn) {
            if (biometricEnabled) {
                navController.navigate(NavRoutes.SECURE_UNLOCK) {
                    popUpTo(NavRoutes.SPLASH) { inclusive = true }
                }
            } else {
                navController.navigate(NavRoutes.HOME) {
                    popUpTo(NavRoutes.SPLASH) { inclusive = true }
                }
            }
        } else {
            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(NavRoutes.SPLASH) { inclusive = true }
            }
        }
    }

    SplashContent()
}
@Composable
fun SplashContent() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column (modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){

            Image(
                painter = painterResource(id = R.drawable.stashlogo),
                contentDescription = "App Logo",
                modifier = Modifier.height(170.dp)

            )

            Text(
                text = "Stash",
                fontSize = 35.sp,
                color = colorResource(R.color.text_primary),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    SplashContent()
}
