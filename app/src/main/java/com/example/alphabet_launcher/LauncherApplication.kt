package com.example.alphabet_launcher

import android.app.Application
import com.example.alphabet_launcher.di.AppContainer

class LauncherApplication: Application() {
    val appContainer: AppContainer by lazy { AppContainer(this) }
}