package org.trainingsession.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.trainingsession.project.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "TrainingSession",
    ) {
        App()
    }
}