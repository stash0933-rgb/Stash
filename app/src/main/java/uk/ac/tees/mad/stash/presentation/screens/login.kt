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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.stash.model.UserData
import uk.ac.tees.mad.stash.navigation.NavRoutes
import uk.ac.tees.mad.stash.presentation.ViewModel.AppViewModel

@Composable
fun LoginScreen(
    viewModel: AppViewModel,
    navController: NavController
) {

    val state = viewModel.loginScreenState.value

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Navigate on success
    LaunchedEffect(state.success) {
        if (state.success) {
            navController.navigate(NavRoutes.HOME) {
                popUpTo(NavRoutes.LOGIN) { inclusive = true }
            }
            viewModel.resetLoginState()
        }
    }

    LoginContent(
        email = email,
        onEmailChange = {
            email = it
            errorMessage = ""
        },
        password = password,
        onPasswordChange = {
            password = it
            errorMessage = ""
        },
        isLoading = state.isLoading,
        errorMessage = state.error ?: errorMessage,
        onLoginClick = {
            if (email.isBlank() || password.isBlank()) {
                errorMessage = "All fields are required"
            } else {
                viewModel.loginUser(
                    UserData(email = email, password = password)
                )
            }
        },
        onSignUpClick = {
            navController.navigate(NavRoutes.SIGNUP)
        }
    )
}

@Composable
fun LoginContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Stash", fontSize = 34.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Secure access to your personal records", color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!errorMessage.isNullOrEmpty()) {
            Text(errorMessage, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLoginClick,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Login", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onSignUpClick) {
            Text("Don't have an account? Sign Up")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginContent(
        email = "",
        onEmailChange = {},
        password = "",
        onPasswordChange = {},
        isLoading = false,
        errorMessage = null,
        onLoginClick = {},
        onSignUpClick = {}
    )
}
