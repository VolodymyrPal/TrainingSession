package org.trainingsession.project.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.trainingsession.project.presentation.composables.SequentialProgressState
import org.trainingsession.project.presentation.composables.SequentialProgressView
import org.trainingsession.project.presentation.composables.Stepper
import org.trainingsession.project.presentation.composables.rememberSequentialProgressState
import org.trainingsession.project.presentation.models.toPresentation
import org.trainingsession.project.presentation.viewModels.ExerciseScreenViewModel
import trainingsession.composeapp.generated.resources.Res
import trainingsession.composeapp.generated.resources.arrow_back
import trainingsession.composeapp.generated.resources.arrow_forward
import trainingsession.composeapp.generated.resources.pause
import trainingsession.composeapp.generated.resources.play


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlayerScreen(
    onBackClick: () -> Unit,
    viewModel: ExerciseScreenViewModel = koinViewModel(
        parameters = { parametersOf(1) }
    )
) {

    val screenState = viewModel.state.collectAsStateWithLifecycle()
    val program = screenState.value.chosenProgram.toPresentation()

    val stepperList: List<Stepper> = program.exercisePresentations.map { s ->
        object : Stepper {
            override val durationMS: Long
                get() = s.durationSeconds.toLong() * 30
        }
    }

    val state = rememberSequentialProgressState(stepperList)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = screenState.value.programName,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(Res.drawable.arrow_back),
                            "Previous",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent,
        contentColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Прогресс-бар
            Column(modifier = Modifier.fillMaxWidth()) {
                ProgressRow(
                    state = state,
                )
                Text(
                    text = "Упражнение ${state.currentStepIndex + 1} из ${state.totalSteps}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Центральный блок с упражнением
            Card(
                modifier = Modifier.size(280.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
//                    Icon(
//                        painter = painterResource(program.icon),
//                        contentDescription = null,
//                        modifier = Modifier.size(72.dp),
//                        tint = MaterialTheme.colorScheme.onPrimaryContainer
//                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Push Ups",//currentExercise.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Try to do it fast",//currentExercise.description,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = state.currentStepElapsedTime.toString(),//formatTime(timeLeft),
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 56.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Кнопки управления
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = {
                        if (state.hasPreviousStep) {
                            state.previous()
                        }
                    },
                    enabled = state.hasPreviousStep,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        painterResource(Res.drawable.arrow_back),
                        "Назад",
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                FilledTonalButton(
                    onClick = { state.playPause() },
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        if (state.isPlaying) painterResource(Res.drawable.pause)
                        else painterResource(Res.drawable.play),
                        if (state.isPlaying) "Играть" else "Пауза",
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                OutlinedButton(
                    onClick = {
                        if (state.hasNextStep) {
                            state.next()
                        }
                    },
                    enabled = state.hasNextStep,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        painterResource(Res.drawable.arrow_forward),
                        "Следующее",
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProgressRow(
    state: SequentialProgressState<Stepper>,
) {
    SequentialProgressView(
        modifier = Modifier.fillMaxWidth(),
        state = state,
    )
}

fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    val formattedMins = mins.toString().padStart(2, '0')
    val formattedSecs = secs.toString().padStart(2, '0')
    return "$formattedMins:$formattedSecs"
}