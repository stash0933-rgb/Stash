package uk.ac.tees.mad.stash.presentation.screens



import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.stash.navigation.NavRoutes
import uk.ac.tees.mad.stash.presentation.ViewModel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun SettingsScreen(
    viewModel: AppViewModel,
    navController: NavController
) {
    val biometricEnabled by viewModel.biometricEnabled.collectAsState()

    SettingsScreenContent(
        biometricEnabled = biometricEnabled,
        onBiometricToggle = { enabled ->
            viewModel.toggleBiometric(enabled)
        },
        onBackClick = { navController.popBackStack() },
        onLogoutClick = {
            viewModel.logoutUser()

            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(0)
            }
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    biometricEnabled: Boolean = false,
    onBiometricToggle: (Boolean) -> Unit = {},
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Security Section
            Text(
                text = "Security",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Enable Biometric Lock",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Require fingerprint or face unlock when opening app",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = biometricEnabled,
                    onCheckedChange = onBiometricToggle
                )
            }

            Divider()

            // Account Section
            Text(
                text = "Account",
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = onLogoutClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreenContent(
        biometricEnabled = true,
        onBackClick = {},
        onLogoutClick = {}
    )
}


