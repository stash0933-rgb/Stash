package uk.ac.tees.mad.stash.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.stash.R
import uk.ac.tees.mad.stash.model.RecordModel
import uk.ac.tees.mad.stash.presentation.ViewModel.AppViewModel

@Composable
fun RecordScreen(
    recordId: String?,
    viewModel: AppViewModel,
    navController: NavController
) {

    val state = viewModel.recordScreenState.value

    var title by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf("") }

    val isEditMode = recordId != null


    LaunchedEffect(recordId) {
        if (recordId != null) {
            viewModel.getRecordById(recordId)
        }
    }


    LaunchedEffect(state.record) {
        state.record?.let {
            title = it.title
            value = it.value
        }
    }

    LaunchedEffect(state.success) {
        if (state.success) {
            viewModel.resetRecordState()
            navController.popBackStack()
        }
    }

    RecordContent(
        title = title,
        onTitleChange = {
            title = it
            localError = ""
        },
        value = value,
        onValueChange = {
            value = it
            localError = ""
        },
        isLoading = state.isLoading,
        errorMessage = state.error ?: localError,
        isEditMode = isEditMode,
        onSaveClick = {
            if (title.isBlank() || value.isBlank()) {
                localError = "All fields required"
            } else {
                if (isEditMode) {
                    viewModel.updateRecord(
                        RecordModel(
                            recordID = recordId!!,
                            title = title,
                            value = value
                        )
                    )
                } else {
                    viewModel.addRecord(
                        RecordModel(
                            recordID = java.util.UUID.randomUUID().toString(),
                            title = title,
                            value = value
                        )
                    )
                }
            }
        },
        onDeleteClick = {
            recordId?.let {
                viewModel.deleteRecord(it)
            }
        }
    )
}

@Composable
fun RecordContent(
    title: String,
    onTitleChange: (String) -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    isEditMode: Boolean,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit
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
                    text = if (isEditMode) "Edit Record" else "Add Record",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colorResource(R.color.primary_dark_navy)
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.primary_navy),
                        focusedLabelColor = colorResource(R.color.primary_navy),
                        cursorColor = colorResource(R.color.primary_navy)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = { Text("Value") },
                    singleLine = true,
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
                    onClick = onSaveClick,
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
                        Text(if (isEditMode) "Update Record" else "Save Record")
                    }
                }

                if (isEditMode) {
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onDeleteClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = colorResource(R.color.error_red)
                        )
                    ) {
                        Text("Delete Record")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddRecordPreview() {
    RecordContent(
        title = "",
        onTitleChange = {},
        value = "",
        onValueChange = {},
        isLoading = false,
        errorMessage = null,
        isEditMode = false,
        onSaveClick = {},
        onDeleteClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun EditRecordPreview() {
    RecordContent(
        title = "Savings",
        onTitleChange = {},
        value = "₹10,000",
        onValueChange = {},
        isLoading = false,
        errorMessage = null,
        isEditMode = true,
        onSaveClick = {},
        onDeleteClick = {}
    )
}