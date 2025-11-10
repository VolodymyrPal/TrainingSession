package org.trainingsession.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.trainingsession.project.di.initKoin
import java.awt.Dimension

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "TrainingSession",
        ) {
            window.minimumSize = Dimension(300, 500)
            App()
        }
    }
}