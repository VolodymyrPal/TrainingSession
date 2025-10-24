package org.trainingsession.project

import androidx.compose.ui.window.ComposeUIViewController
import org.trainingsession.project.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }