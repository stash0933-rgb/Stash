package uk.ac.tees.mad.stash.presentation.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.tees.mad.stash.domain.Repo
import uk.ac.tees.mad.stash.model.ResultState
import uk.ac.tees.mad.stash.model.UserData

class AppViewModel(
    private val repo: Repo,
    private val savedStateHandle: SavedStateHandle

) : ViewModel() {

    private val _signupScreenState = mutableStateOf(SignUpScreenState())
    val signupScreenState = _signupScreenState

    private val _loginScreenState = mutableStateOf(LogInScreenState())
    val loginScreenState = _loginScreenState
    val passedEmail: String? = savedStateHandle["email"]


    fun loginUser(userData: UserData) {
        viewModelScope.launch {
            repo.loginuserwithemailandpassword(userData).collect { result ->
                when (result) {

                    ResultState.Loading -> {
                        _loginScreenState.value =
                            LogInScreenState(isLoading = true)
                    }

                    is ResultState.Succes -> {
                        _loginScreenState.value =
                            LogInScreenState(
                                success = true,
                                userdata = result.data
                            )
                    }

                    is ResultState.error -> {
                        _loginScreenState.value =
                            LogInScreenState(
                                error = result.message
                            )
                    }
                }
            }
        }
    }

    fun registerUser(userData: UserData) {
        viewModelScope.launch {
            repo.registeruserwithemailandpassword(userData).collect { result ->
                when (result) {

                    ResultState.Loading -> {
                        _signupScreenState.value =
                            SignUpScreenState(isLoading = true)
                    }

                    is ResultState.Succes -> {
                        _signupScreenState.value =
                            SignUpScreenState(
                                success = true,
                                userdata = result.data
                            )
                    }

                    is ResultState.error -> {
                        _signupScreenState.value =
                            SignUpScreenState(
                                error = result.message
                            )
                    }
                }
            }
        }
    }
}
data class SignUpScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userdata: String? = null,
    val success: Boolean = false
)

data class LogInScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userdata: String? = null,
    val success: Boolean = false
)
