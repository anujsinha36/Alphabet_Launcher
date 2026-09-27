package com.example.alphabet_launcher.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.alphabet_launcher.domain.AlphabetCurve
import com.example.alphabet_launcher.ui.LauncherViewModel
import com.example.alphabet_launcher.ui.theme.LauncherBackground
import com.example.alphabet_launcher.ui.theme.TextSecondary

@Composable
fun LauncherScreen(viewModel: LauncherViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var screenWidthPx by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LauncherBackground)
            .onSizeChanged { screenWidthPx = it.width },
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = TextSecondary,
            )
        } else {
            val selectedLetter = state.selectedLetter
            if (selectedLetter != null) {
                LetterContent(
                    letter = selectedLetter,
                    apps = state.selectedApps,
                    onAppClick = viewModel::onAppClick,
                )
            } else {
                HomeContent(
                    favourites = state.favourites,
                    onAppClick = viewModel::onAppClick,
                )
            }

            AlphabetBar(
                maxShiftPx = screenWidthPx * AlphabetCurve.MAX_SHIFT_FRACTION,
                onSelectedLetterChange = viewModel::onSelectedLetterChange,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .padding(vertical = 100.dp)
                    .width(BAR_WIDTH_DP),
            )
        }
    }
}

private val BAR_WIDTH_DP = 44.dp
