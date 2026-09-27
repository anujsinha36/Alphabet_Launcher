package com.example.alphabet_launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alphabet_launcher.ui.LauncherViewModel
import com.example.alphabet_launcher.ui.screen.LauncherScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val container = (application as LauncherApplication).appContainer
            val viewModel: LauncherViewModel = viewModel(
                factory = LauncherViewModel.factory(container.installedAppsRepository),
            )
            LauncherScreen(viewModel)

        }
    }
}



