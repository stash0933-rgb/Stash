package uk.ac.tees.mad.stash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavBackStackEntry
import uk.ac.tees.mad.stash.data.Repo.RepoImpl
import uk.ac.tees.mad.stash.presentation.ViewModel.AppViewModel

class AppViewModelFactory(
    private val backStackEntry: NavBackStackEntry
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return AppViewModel(
            repo = RepoImpl(),
            savedStateHandle = backStackEntry.savedStateHandle
        ) as T
    }
}
