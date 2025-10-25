package org.trainingsession.project

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import org.trainingsession.project.di.appModule
import org.trainingsession.project.di.commonModule

fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin {
            modules(appModule, commonModule)
        }
    }
) { App() }