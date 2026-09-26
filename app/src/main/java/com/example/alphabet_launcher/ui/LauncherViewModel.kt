package com.example.alphabet_launcher.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.alphabet_launcher.data.AppInfo
import com.example.alphabet_launcher.data.InstalledAppsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class LauncherUiState(
    val isLoading: Boolean = true,
    val favourites: List<AppInfo> = emptyList(),
    val appsByLetter: Map<Char, List<AppInfo>> = emptyMap(),
    val selectedLetter: Char? = null,
) {
    val isBrowsing: Boolean get() = selectedLetter != null

    val selectedApps: List<AppInfo>
        get() = selectedLetter?.let { appsByLetter[it] }.orEmpty()
}

class LauncherViewModel(
    private val installedAppsRepository: InstalledAppsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LauncherUiState())
    val uiState: StateFlow<LauncherUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val apps = installedAppsRepository.getInstalledApps()

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    favourites = apps.take(FAVOURITE_COUNT),
                    appsByLetter = apps
                        .groupBy { it.label.first().uppercaseChar() }
                        .filterKeys { it in 'A'..'Z' },
                )
            }
        }
    }

    fun onSelectedLetterChange(letter: Char?) {
        _uiState.update { it.copy(selectedLetter = letter) }
    }

    fun onAppClick(app: AppInfo) {
        installedAppsRepository.launchApp(app)
    }

    companion object {
        const val FAVOURITE_COUNT = 6

        fun factory(installedAppsRepository: InstalledAppsRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { LauncherViewModel(installedAppsRepository) }
            }
    }
}
