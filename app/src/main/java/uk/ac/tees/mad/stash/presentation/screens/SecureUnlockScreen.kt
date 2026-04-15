package uk.ac.tees.mad.stash.presentation.screens

import android.content.Context
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.stash.R
import uk.ac.tees.mad.stash.navigation.NavRoutes

@Composable
fun SecureUnlockScreen(
    navController: NavController,
    onAuthSuccess: suspend () -> Unit = {}
) {

    val context = LocalContext.current
    val activity = context as? FragmentActivity

    var authState by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var promptShown by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        Log.d("SecureUnlock", "Screen launched")

        if (activity == null) {
            Log.e("SecureUnlock", "Activity is null - cannot show biometric prompt")
            errorMessage = "Unable to initialize biometric authentication"
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
                        Log.d("SecureUnlock", "Authentication succeeded")
                        authState = true
                    }

                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence
                    ) {
                        Log.d("SecureUnlock", "Authentication error: $errorCode - $errString")
                        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                            errorCode == BiometricPrompt.ERROR_USER_CANCELED
                        ) {
                            errorMessage = "Authentication cancelled. Please try again."
                        } else {
                            errorMessage = errString.toString()
                        }
                    }

                    override fun onAuthenticationFailed() {
                        Log.d("SecureUnlock", "Authentication failed")
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

            Log.d("SecureUnlock", "Showing biometric prompt")
            biometricPrompt.authenticate(promptInfo)
            promptShown = true

        } else {
            // Biometric not available, show error
            Log.e("SecureUnlock", "Biometric not available: $canAuthenticate")
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

    // Navigate to Home on successful authentication
    if (authState) {
        LaunchedEffect(Unit) {
            Log.d("SecureUnlock", "Navigating to HOME after successful auth")
            onAuthSuccess() // Callback to update timestamp
            navController.navigate(NavRoutes.HOME) {
                popUpTo(NavRoutes.SECURE_UNLOCK) { inclusive = true }
            }
        }
    }

    SecureUnlockContent(
        errorMessage = errorMessage,
        onRetry = {
            Log.d("SecureUnlock", "Retry clicked")
            errorMessage = null
            // Restart the screen by navigating to itself
            navController.navigate(NavRoutes.SECURE_UNLOCK) {
                popUpTo(NavRoutes.SECURE_UNLOCK) { inclusive = true }
            }
        },
        onCancel = {
            Log.d("SecureUnlock", "Cancel clicked")
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

    Scaffold(
        containerColor = colorResource(R.color.background_main)
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {

            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.background_card)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (errorMessage != null) {

                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = colorResource(R.color.error_red),
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Authentication Failed",
                            style = MaterialTheme.typography.headlineSmall,
                            color = colorResource(R.color.primary_dark_navy)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorResource(R.color.text_secondary)
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            OutlinedButton(
                                onClick = onCancel,
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Cancel")
                            }

                            Button(
                                onClick = onRetry,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.primary_dark_navy),
                                    contentColor = colorResource(R.color.text_white)
                                )
                            ) {
                                Text("Retry")
                            }
                        }

                    } else {

                        Text(
                            text = "Secure Unlock",
                            style = MaterialTheme.typography.headlineMedium,
                            color = colorResource(R.color.primary_dark_navy)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Authenticate using fingerprint, face recognition, or device PIN to access your secure vault.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorResource(R.color.text_secondary)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        CircularProgressIndicator(
                            color = colorResource(R.color.primary_dark_navy)
                        )
                    }
                }
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
