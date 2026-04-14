package uk.ac.tees.mad.stash.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.stash.R

import uk.ac.tees.mad.stash.presentation.ViewModel.AppViewModel
import uk.ac.tees.mad.stash.model.UserData
import uk.ac.tees.mad.stash.navigation.NavRoutes

@Composable
fun SignupScreen(
    navController: androidx.navigation.NavController,
    viewModel: AppViewModel
) {
    val state = viewModel.signupScreenState.value

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // React to state changes (success)
    LaunchedEffect(state.success) {
        if (state.success) {
            // Navigate back to Login with email argument
            navController.navigate(NavRoutes.LOGIN) {
                // Clear backstack to avoid loop? Or keep it simple.
                // popping up to LOGIN route to clear stack but allow back nav if desired.
                popUpTo(NavRoutes.LOGIN) { inclusive = true }
            }
        }
    }

    SignupContent(
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = { confirmPassword = it },
        isLoading = state.isLoading,
        errorMessage = if (state.error != null) state.error else errorMessage,
        onRegisterClick = {
             if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                errorMessage = "All fields are required"
            } else if (password != confirmPassword) {
                errorMessage = "Passwords do not match"
            } else if (password.length < 6) {
                errorMessage = "Password must be at least 6 characters"
            } else {
                errorMessage = ""
                // Call ViewModel registration
                viewModel.registerUser(
                    UserData(
                        email = email,
                        password = password
                    )
                )
            }
        },
        onLoginClick = {
            navController.navigate(NavRoutes.LOGIN)
        }
    )
}

@Composable
fun SignupContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_main)),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.background_card)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colorResource(R.color.primary_dark_navy)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Secure your personal vault",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.text_secondary)
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.primary_navy),
                        focusedLabelColor = colorResource(R.color.primary_navy),
                        cursorColor = colorResource(R.color.primary_navy)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.primary_navy),
                        focusedLabelColor = colorResource(R.color.primary_navy),
                        cursorColor = colorResource(R.color.primary_navy)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = { Text("Confirm Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.primary_navy),
                        focusedLabelColor = colorResource(R.color.primary_navy),
                        cursorColor = colorResource(R.color.primary_navy)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (!errorMessage.isNullOrEmpty()) {
                    Text(
                        text = errorMessage,
                        color = colorResource(R.color.error_red),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onRegisterClick,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.primary_dark_navy),
                        contentColor = colorResource(R.color.text_white)
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = colorResource(R.color.text_white),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Create Account")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onLoginClick) {
                    Text(
                        "Already have an account? Login",
                        color = colorResource(R.color.primary_navy)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    SignupContent(
        email = "",
        onEmailChange = {},
        password = "",
        onPasswordChange = {},
        confirmPassword = "",
        onConfirmPasswordChange = {},
        isLoading = false,
        errorMessage = null,
        onRegisterClick = {},
        onLoginClick = {}
    )
}
