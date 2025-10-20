package org.trainingsession.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.trainingsession.project.mainScreen.ProgramSelectionScreen
import org.trainingsession.project.mainScreen.WorkoutPlayerScreen
import org.trainingsession.project.mainScreen.WorkoutProgramPresentation
import org.trainingsession.project.mainScreen.theme.AppTheme

@Composable
@Preview
fun App() {
    AppTheme {
        var selectedProgram by remember { mutableStateOf<WorkoutProgramPresentation?>(null) }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (selectedProgram == null) {
                ProgramSelectionScreen(
                    onProgramSelected = { selectedProgram = it }
                )
            } else {
                WorkoutPlayerScreen(
                    program = selectedProgram!!,
                    onBackClick = { selectedProgram = null }
                )
            }
        }
    }
}