package org.trainingsession.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin
import org.trainingsession.project.actual.AppLogger

fun main() = application {
    startKoin { modules(appModule) }
    Window(
        onCloseRequest = ::exitApplication,
        title = "TrainingSession",
    ) {
        App()
    }
}