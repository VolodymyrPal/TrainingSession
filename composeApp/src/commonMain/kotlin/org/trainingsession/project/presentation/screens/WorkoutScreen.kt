package org.trainingsession.project.presentation.screens

import Square
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.trainingsession.project.presentation.composables.SequentialProgress
import org.trainingsession.project.presentation.viewModels.WorkoutScreenViewModel
import org.trainingsession.project.resources.Restore
import trainingsession.composeapp.generated.resources.Res
import trainingsession.composeapp.generated.resources.arrow_back
import trainingsession.composeapp.generated.resources.arrow_forward
import trainingsession.composeapp.generated.resources.indexFrom
import trainingsession.composeapp.generated.resources.pause
import trainingsession.composeapp.generated.resources.play
import kotlin.math.ceil


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlayerScreen(
    onBackClick: () -> Unit, viewModel: WorkoutScreenViewModel = koinViewModel(
        parameters = { parametersOf(1) })
) {
    val screenState = viewModel.state.collectAsStateWithLifecycle()

    val formattedTime = remember {
        derivedStateOf {
            val currentStep = screenState.value.stepperList[screenState.value.currentIndex]
            val timeLeftMs = currentStep.durationMS - currentStep.elapsedTime.value
            val safeTimeMs = maxOf(0L, timeLeftMs)
            val totalSecondsLong = safeTimeMs / 1000.0
            val totalSeconds = ceil(totalSecondsLong).toLong()
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
    val pagerState = rememberPagerState(
        0, 0f, pageCount = { screenState.value.stepperList.size })


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Back to programs",
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Unspecified,
                        color = MaterialTheme.colorScheme.primary
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
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }, containerColor = Color.Transparent, contentColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
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
                    text = stringResource(
                        Res.string.indexFrom,
                        screenState.value.currentIndex + 1,
                        screenState.value.stepperList.size
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                LeftTimeBar(
                    modifier = Modifier.fillMaxWidth(), timeLeft = formattedTime.value
                )
            }


            Card(
                modifier = Modifier.padding(24.dp).weight(1f),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        text = screenState.value.stepperList[screenState.value.currentIndex].name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    HorizontalPager(
                        modifier = Modifier.weight(1f),
                        state = pagerState,

                        ) {
                        Text(
                            text = screenState.value.stepperList[screenState.value.currentIndex].description,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                    Row(
                        Modifier.wrapContentHeight().fillMaxWidth().padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(pagerState.pageCount) { iteration ->
                            val color =
                                if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.4f
                                )
                            Box(
                                modifier = Modifier.padding(2.dp).clip(CircleShape)
                                    .background(color).size(8.dp)
                            )
                        }
                    }

                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.previousStep()
                    }, enabled = screenState.value.currentIndex > 0,
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
                            onClick = { viewModel.resetCurrentStep() }, modifier = Modifier
                        ) {
                            Icon(
                                Restore,
                                "Reset current step",
                                modifier = Modifier,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(
                            onClick = { viewModel.resetWorkout() }, modifier = Modifier
                        ) {
                            Icon(
                                Square,
                                "Reset all program",
                                modifier = Modifier,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    FilledIconButton(
                        onClick = { viewModel.playPause() },
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        colors = IconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)
                        )
                    ) {
                        Icon(
                            if (screenState.value.isPlaying) painterResource(Res.drawable.pause)
                            else painterResource(Res.drawable.play),
                            if (screenState.value.isPlaying) "Играть" else "Пауза",
                            modifier = Modifier.fillMaxSize().padding(24.dp),
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
        }
    }
}

@Composable
fun LeftTimeBar(
    modifier: Modifier = Modifier,
    timeLeft: String,
) {
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        text = timeLeft,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

fun formatTimeKMP(minutes: Long, seconds: Long): String {
    val minutesString = minutes.toString().padStart(2, '0')
    val secondsString = seconds.toString().padStart(2, '0')
    return "$minutesString:$secondsString"
}

fun formatTimeKMP(hours: Long, minutes: Long, seconds: Long): String {
    val hoursString = hours.toString().padStart(2, '0')
    val minutesString = minutes.toString().padStart(2, '0')
    val secondsString = seconds.toString().padStart(2, '0')
    return "$hoursString:$minutesString:$secondsString"
}