package uk.ac.tees.mad.stash.presentation.screens



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.stash.R
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
        containerColor = colorResource(R.color.background_main),

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        color = colorResource(R.color.text_primary)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",

                        )
                    }
                },

            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {


            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.background_card)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        text = "Security",
                        style = MaterialTheme.typography.titleMedium,
                        color = colorResource(R.color.primary_dark_navy)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(modifier = Modifier.weight(1f)) {

                            Text(
                                text = "Enable Biometric Lock",
                                style = MaterialTheme.typography.bodyLarge,
                                color = colorResource(R.color.text_primary)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Require fingerprint or face unlock when opening app",
                                style = MaterialTheme.typography.bodySmall,
                                color = colorResource(R.color.text_secondary)
                            )
                        }

                        Switch(
                            checked = biometricEnabled,
                            onCheckedChange = onBiometricToggle,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colorResource(R.color.primary_dark_navy),
                                checkedTrackColor = colorResource(R.color.primary_light_navy)
                            )
                        )
                    }
                }
            }


            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.background_card)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        text = "Account",
                        style = MaterialTheme.typography.titleMedium,
                        color = colorResource(R.color.primary_dark_navy)
                    )

                    Button(
                        onClick = onLogoutClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.error_red),
                            contentColor = colorResource(R.color.text_white)
                        )
                    ) {
                        Text("Logout")
                    }
                }
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


