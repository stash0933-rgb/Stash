package uk.ac.tees.mad.stash.presentation.screens



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.stash.model.RecordModel
import uk.ac.tees.mad.stash.presentation.ViewModel.AppViewModel
import uk.ac.tees.mad.stash.presentation.ViewModel.HomeScreenState

@Composable
fun HomeScreenContent(
    state: HomeScreenState,
    onRecordClick: (RecordModel) -> Unit,
    onAddClick: () -> Unit
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Record")
            }
        }
    ) { padding ->

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = androidx.compose.ui.Alignment.Center
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

    HomeScreenContent(
        state = state,
        onRecordClick = { record ->
            navController.navigate(uk.ac.tees.mad.stash.navigation.NavRoutes.recordRoute(record.recordID))
        },
        onAddClick = {
            navController.navigate(uk.ac.tees.mad.stash.navigation.NavRoutes.recordRoute(null))
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
        onAddClick = {}
    )
}
