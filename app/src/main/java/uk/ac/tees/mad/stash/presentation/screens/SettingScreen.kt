package uk.ac.tees.mad.stash.presentation.screens



import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

    SettingsScreenContent(
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
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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
        onBackClick = {},
        onLogoutClick = {}
    )
}

