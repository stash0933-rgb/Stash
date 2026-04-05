package uk.ac.tees.mad.stash.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.stash.model.RecordModel
import uk.ac.tees.mad.stash.navigation.NavRoutes
import uk.ac.tees.mad.stash.presentation.ViewModel.AppViewModel
import uk.ac.tees.mad.stash.presentation.ViewModel.HomeScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    state: HomeScreenState,
    onRecordClick: (RecordModel) -> Unit,
    onAddClick: () -> Unit,
    onSettingsClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stash") },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Record"
                )
            }
        }
    ) { padding ->

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.error)
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.userdata ?: emptyList()) { record ->

                        Card(
                            onClick = { onRecordClick(record) }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = record.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = record.value,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    navController: NavController
) {

    val state = viewModel.homeScreenState.value
    val biometricEnabled by viewModel.biometricEnabled.collectAsState()
    
    // Get the previous route to check if we came from SecureUnlock
    val previousRoute = navController.previousBackStackEntry?.destination?.route

    // Check session timeout ONLY if not coming from SecureUnlock
    // Check session timeout - DISABLED (User request: Only ask on restart)
    LaunchedEffect(Unit) {
        // Just keep the timestamp updated
        viewModel.updateLastActiveTimestamp()
    }

    HomeScreenContent(
        state = state,
        onRecordClick = { record ->
            navController.navigate(
                NavRoutes.recordRoute(record.recordID)
            )
        },
        onAddClick = {
            navController.navigate(
                NavRoutes.recordRoute(null)
            )
        },
        onSettingsClick = {
            navController.navigate(NavRoutes.SETTINGS)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {

    val fakeState = HomeScreenState(
        isLoading = false,
        userdata = listOf(
            RecordModel("1", "Savings", "₹10,000"),
            RecordModel("2", "Investments", "₹25,000")
        )
    )

    HomeScreenContent(
        state = fakeState,
        onRecordClick = {},
        onAddClick = {},
        onSettingsClick = {}
    )
}
