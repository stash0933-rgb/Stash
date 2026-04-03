package uk.ac.tees.mad.stash.presentation.screens

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import uk.ac.tees.mad.stash.navigation.NavRoutes

@Composable
fun SecureUnlockScreen(
    navController: NavController
) {

    val context = LocalContext.current
    val activity = context as FragmentActivity

    var authState by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        val biometricManager = BiometricManager.from(context)

        val canAuthenticate = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
                    or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )

        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {

            val executor = ContextCompat.getMainExecutor(context)

            val biometricPrompt = BiometricPrompt(
                activity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        authState = true
                    }

                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence
                    ) {
                        navController.navigate(NavRoutes.LOGIN) {
                            popUpTo(0)
                        }
                    }
                }
            )

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Unlock Stash")
                .setSubtitle("Authenticate to access your secure records")
                .setAllowedAuthenticators(
                    BiometricManager.Authenticators.BIOMETRIC_STRONG
                            or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
                .build()

            biometricPrompt.authenticate(promptInfo)

        } else {
            navController.navigate(NavRoutes.HOME)
        }
    }

    if (authState) {
        navController.navigate(NavRoutes.HOME) {
            popUpTo(NavRoutes.SECURE_UNLOCK) { inclusive = true }
        }
    }

    SecureUnlockContent()
}

@Composable
fun SecureUnlockContent() {

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Secure Unlock Required",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Please authenticate using fingerprint, face recognition, or device PIN to continue.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator()
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SecureUnlockPreview() {
    SecureUnlockContent()
}
