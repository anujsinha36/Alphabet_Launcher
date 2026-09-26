package com.example.alphabet_launcher.di

import android.content.Context
import com.example.alphabet_launcher.data.InstalledAppsRepository

class AppContainer(context: Context) {
    val installedAppsRepository: InstalledAppsRepository = InstalledAppsRepository(context)
}