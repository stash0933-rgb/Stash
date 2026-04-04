package uk.ac.tees.mad.stash.presentation.screens

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Warning
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
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.stash.navigation.NavRoutes

@Composable
fun SecureUnlockScreen(
    navController: NavController
) {

    val context = LocalContext.current
    val activity = context as? FragmentActivity

    var authState by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {

        if (activity == null) {
            // If not in FragmentActivity context, skip to home
            navController.navigate(NavRoutes.HOME) {
                popUpTo(NavRoutes.SECURE_UNLOCK) { inclusive = true }
            }
            return@LaunchedEffect
        }

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
                        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                            errorCode == BiometricPrompt.ERROR_USER_CANCELED
                        ) {
                            errorMessage = "Authentication cancelled. Please try again."
                        } else {
                            errorMessage = errString.toString()
                        }
                    }

                    override fun onAuthenticationFailed() {
                        errorMessage = "Authentication failed. Please try again."
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
            // Biometric not available, show error
            errorMessage = when (canAuthenticate) {
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                    "No biometric hardware available"
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                    "Biometric hardware unavailable"
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                    "No biometric credentials enrolled"
                else -> "Biometric authentication not available"
            }
        }
    }

    if (authState) {
        LaunchedEffect(Unit) {
            navController.navigate(NavRoutes.HOME) {
                popUpTo(NavRoutes.SECURE_UNLOCK) { inclusive = true }
            }
        }
    }

    SecureUnlockContent(
        errorMessage = errorMessage,
        onRetry = {
            errorMessage = null
            // Restart the screen by navigating to itself
            navController.navigate(NavRoutes.SECURE_UNLOCK) {
                popUpTo(NavRoutes.SECURE_UNLOCK) { inclusive = true }
            }
        },
        onCancel = {
            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(0)
            }
        }
    )
}

@Composable
fun SecureUnlockContent(
    errorMessage: String? = null,
    onRetry: () -> Unit = {},
    onCancel: () -> Unit = {}
) {

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (errorMessage != null) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Authentication Error",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(onClick = onCancel) {
                        Text("Cancel")
                    }
                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                }
            } else {
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
}

@Preview(showBackground = true)
@Composable
fun SecureUnlockPreview() {
    SecureUnlockContent()
}

@Preview(showBackground = true)
@Composable
fun SecureUnlockErrorPreview() {
    SecureUnlockContent(
        errorMessage = "Authentication failed. Please try again."
    )
}
