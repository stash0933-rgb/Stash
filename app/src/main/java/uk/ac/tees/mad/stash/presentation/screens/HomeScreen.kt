package uk.ac.tees.mad.stash.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.stash.R
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
        containerColor = colorResource(R.color.background_main),

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Stash",
                        style = MaterialTheme.typography.titleLarge,
                        color = colorResource(R.color.text_white)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.primary_dark_navy),
                    titleContentColor = colorResource(R.color.text_white),
                    actionIconContentColor = colorResource(R.color.text_white)
                ),
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
                onClick = onAddClick,
                containerColor = colorResource(R.color.primary_navy),
                contentColor = colorResource(R.color.text_white)
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
                    CircularProgressIndicator(
                        color = colorResource(R.color.primary_navy)
                    )
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error,
                        color = colorResource(R.color.error_red),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }


            state.userdata.isNullOrEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No records yet.\nTap + to add securely.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorResource(R.color.text_secondary)
                    )
                }
            }


            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    items(state.userdata!!) { record ->

                        Card(
                            onClick = { onRecordClick(record) },
                            modifier = Modifier.height(120.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(R.color.background_card)
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp)
                            ) {

                                Text(
                                    text = record.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = colorResource(R.color.text_primary)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = record.value,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colorResource(R.color.text_secondary)
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
    

    LaunchedEffect(Unit) {

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
