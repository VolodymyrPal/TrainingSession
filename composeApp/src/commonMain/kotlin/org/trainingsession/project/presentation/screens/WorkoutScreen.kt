package org.trainingsession.project.presentation.screens

import Square
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
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
import org.trainingsession.project.presentation.composables.SequentialProgress
import org.trainingsession.project.presentation.viewModels.WorkoutScreenViewModel
import org.trainingsession.project.resources.Restore
import trainingsession.composeapp.generated.resources.Res
import trainingsession.composeapp.generated.resources.arrow_back
import trainingsession.composeapp.generated.resources.arrow_forward
import trainingsession.composeapp.generated.resources.pause
import trainingsession.composeapp.generated.resources.play


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlayerScreen(
    onBackClick: () -> Unit,
    viewModel: WorkoutScreenViewModel = koinViewModel(
        parameters = { parametersOf(1) }
    )
) {
    val screenState = viewModel.state.collectAsStateWithLifecycle()

    val formattedTime = remember {
        derivedStateOf {
            val currentStep = screenState.value.stepperList[screenState.value.currentIndex]
            val timeLeftMs = currentStep.durationMS - currentStep.elapsedTime.value
            val safeTimeMs = maxOf(0L, timeLeftMs)
            val totalSeconds = safeTimeMs / 1000L
            val minutes = (totalSeconds / 60) % 60
            val seconds = totalSeconds % 60
            val hours = totalSeconds / 3600
            if (hours > 0) {
                val remainingMinutes = minutes % 60
                formatTimeKMP(hours, remainingMinutes, seconds)
            } else {
                formatTimeKMP(minutes, seconds)
            }
        }
    }


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
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SequentialProgress(
                    modifier = Modifier.fillMaxWidth(),
                    currentStepIndex = screenState.value.currentIndex,
                    list = screenState.value.stepperList
                )
                Text(
                    text = "Упражнение ${screenState.value.currentIndex + 1} из ${screenState.value.stepperList}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                LeftTimeBar(
                    modifier = Modifier.fillMaxWidth(),
                    timeLeft = formattedTime.value
                )
            }

            Spacer(modifier = Modifier.weight(1f))

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
                        text = "Nice", //progressState.currentStepElapsedTime.toString(),
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 56.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.previousStep()
                    },
                    enabled = screenState.value.currentIndex > 0,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        painterResource(Res.drawable.arrow_back),
                        "Назад",
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row {
                        IconButton(
                            onClick = { viewModel.resetCurrentStep() },
                            modifier = Modifier
                        ) {
                            Icon(
                                Restore,
                                "Reset current step",
                                modifier = Modifier,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(
                            onClick = { viewModel.resetWorkout() },
                            modifier = Modifier
                        ) {
                            Icon(
                                Square,
                                "Reset all program",
                                modifier = Modifier,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    FilledTonalButton(
                        onClick = { viewModel.playPause() },
                        modifier = Modifier.size(80.dp)
                    ) {
                        Icon(
                            if (screenState.value.isPlaying) painterResource(Res.drawable.pause)
                            else painterResource(Res.drawable.play),
                            if (screenState.value.isPlaying) "Играть" else "Пауза",
                            modifier = Modifier.fillMaxSize(),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }



                OutlinedButton(
                    onClick = {
                        viewModel.nextStep()
                    },
                    enabled = screenState.value.currentIndex < screenState.value.stepperList.size - 1,
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

fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    val formattedMins = mins.toString().padStart(2, '0')
    val formattedSecs = secs.toString().padStart(2, '0')
    return "$formattedMins:$formattedSecs"
}