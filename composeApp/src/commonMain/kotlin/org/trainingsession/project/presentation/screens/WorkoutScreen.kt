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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.trainingsession.project.presentation.composables.WorkoutProgramPresentation
import trainingsession.composeapp.generated.resources.Res
import trainingsession.composeapp.generated.resources.arrow_back
import trainingsession.composeapp.generated.resources.arrow_forward
import trainingsession.composeapp.generated.resources.pause
import trainingsession.composeapp.generated.resources.play


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlayerScreen(
    program: WorkoutProgramPresentation,
    onBackClick: () -> Unit
) {

    // Прогресс бар меняет цвет в зависимости кол-ва шагов

    var currentExerciseIndex by remember { mutableStateOf(0) }
    var isPaused by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(program.exercisePresentations[0].durationSeconds) }

    val currentExercise = program.exercisePresentations[currentExerciseIndex]
    val totalExercises = program.exercisePresentations.size
    val progress =
        (currentExerciseIndex + 1 - (timeLeft.toFloat() / currentExercise.durationSeconds)) / totalExercises

    LaunchedEffect(currentExerciseIndex, isPaused, timeLeft) {
        if (!isPaused && timeLeft > 0) {
            delay(1000)
            timeLeft -= 1
        } else if (timeLeft == 0 && currentExerciseIndex < totalExercises - 1) {
            currentExerciseIndex += 1
            timeLeft = program.exercisePresentations[currentExerciseIndex].durationSeconds
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = program.name,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
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
                Text(
                    text = "Упражнение ${currentExerciseIndex + 1} из $totalExercises",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
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
                        text = currentExercise.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentExercise.description,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = formatTime(timeLeft),
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
                        if (currentExerciseIndex > 0) {
                            currentExerciseIndex -= 1
                            timeLeft =
                                program.exercisePresentations[currentExerciseIndex].durationSeconds
                        }
                    },
                    enabled = currentExerciseIndex > 0,
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
                    onClick = { isPaused = !isPaused },
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        if (!isPaused) painterResource(Res.drawable.pause)
                        else painterResource(Res.drawable.play),
                        if (isPaused) "Играть" else "Пауза",
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                OutlinedButton(
                    onClick = {
                        if (currentExerciseIndex < totalExercises - 1) {
                            currentExerciseIndex += 1
                            timeLeft =
                                program.exercisePresentations[currentExerciseIndex].durationSeconds
                        }
                    },
                    enabled = currentExerciseIndex < totalExercises - 1,
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