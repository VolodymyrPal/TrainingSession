package org.trainingsession.project.presentation.composables

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.trainingsession.project.domain.models.Program
import org.trainingsession.project.presentation.models.ExercisePresentation
import org.trainingsession.project.presentation.models.Stepper
import org.trainingsession.project.presentation.theme.AppTheme
import org.trainingsession.project.presentation.viewModels.ExerciseScreenState
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun SequentialProgress(
    list: List<Stepper> = emptyList(),
    currentStepIndex: Int = 0,
    previewCountRight: Int = 1,
    previewCountLeft: Int = 0,
    modifier: Modifier = Modifier,
    lineHeight: Dp = 4.dp
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            (0 until currentStepIndex).forEach { index ->
                val toShow = derivedStateOf { index >= currentStepIndex - previewCountLeft }
                val isCompleted =
                    derivedStateOf { list[index].durationMS == list[index].elapsedTime.value }

                key(index) {
                    ProgressDot(
                        isCompleted = isCompleted.value,
                        toShow = toShow.value,
                        modifier = Modifier
                    )
                }
            }
        }

        if (list.isNotEmpty() && currentStepIndex < list.size) {
            ProgressConnector(
                progress = { list[currentStepIndex].progress.value },
                modifier = Modifier.padding(4.dp).weight(1f),
                lineHeight = lineHeight
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            (currentStepIndex until list.size).forEach { index ->
                val isCurrent = index == currentStepIndex
                val isUpcoming =
                    index > currentStepIndex && index <= currentStepIndex + previewCountRight
                val toShow = isCurrent || isUpcoming
                val isCompleted =
                    derivedStateOf { list[index].durationMS == list[index].elapsedTime.value }

                key(index) {
                    ProgressDot(
                        isCompleted = isCompleted.value,
                        toShow = toShow,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressConnector(
    progress: () -> Float,
    modifier: Modifier = Modifier,
    lineHeight: Dp = 6.dp,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    surfaceColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    val progress = { progress().coerceIn(0f, 1f) }

    Canvas(modifier.height(lineHeight).width(IntrinsicSize.Max)) {
        val progressWidth = size.width * progress()

        drawLine(
            color = surfaceColor,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = size.height,
            cap = StrokeCap.Round
        )
        val startFraction = 0.2f
        val endFraction = 0.7f
        val scaledFraction = startFraction + (endFraction - startFraction) * progress()

        if (progressWidth > 0) {
            drawLine(
                color = primaryColor,
                start = Offset(0f, size.height / 2),
                end = Offset(progressWidth, size.height / 2),
                strokeWidth = size.height * scaledFraction,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun ProgressDot(
    isCompleted: Boolean,
    toShow: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 8.dp,
    lineWidth: Dp = 3.dp,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    surfaceColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
) {
    val transition = updateTransition(targetState = toShow, label = "DotTransition")

    val animatedWidth by transition.animateDp(
        transitionSpec = { spring() }, label = "WidthAnimation"
    ) { isVisible -> if (isVisible) size else lineWidth }

    val animatedCornerRadius by transition.animateDp(
        transitionSpec = { spring() }, label = "CornerRadiusAnimation"
    ) { isVisible -> if (isVisible) size / 2 else 1.dp }

    val animatedWidthModifier = modifier.layout { measurable, constraints ->
        val targetWidthPx = animatedWidth.roundToPx()
        val fixedHeightPx = size.roundToPx()
        val placeable = measurable.measure(
            Constraints.fixed(targetWidthPx, fixedHeightPx)
        )
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }

    Canvas(
        modifier = animatedWidthModifier
    ) {
        drawRoundRect(
            color = if (isCompleted) primaryColor else surfaceColor,
            size = Size(this.size.width, this.size.height),
            cornerRadius = CornerRadius(animatedCornerRadius.toPx())
        )
    }
}

@Preview
@Composable
fun ProgressPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            var state = ExerciseScreenState(
                programName = "",
                chosenProgram = Program(
                    id = 0,
                    description = "",
                    name = "",
                    programExercises = emptyList()
                ),
                currentIndex = 0,
                isPlaying = false,
                isLoading = true,
                stepperList = listOf(
                    ExercisePresentation(
                        name = "Exercise",
                        durationSeconds = 60,
                        description = "Do hard, not dumb",
                    ),
                    ExercisePresentation(
                        name = "Something",
                        durationSeconds = 60,
                        description = "Do smart, not hard"
                    )
                )
            )
            state.stepperList[0].progress.value = 0.5f
            state.stepperList[1].progress.value = 0.7f
            state.stepperList[1].elapsedTime.value = 3000
            state.stepperList[0].elapsedTime.value = 3000

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val currentStep = state.stepperList.random()
                    if (currentStep != null) {
                        Text(
                            text = "${currentStep.name} (${state.currentIndex + 1}/${state.stepperList.size})",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "${currentStep.elapsedTime.value}ms / ${currentStep.durationMS}ms (${(currentStep.progress.value * 100).toInt()}%)",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        Spacer(
                            modifier = Modifier.height(
                                with(LocalDensity.current) {
                                    MaterialTheme.typography.headlineMedium.lineHeight.toDp() +
                                            MaterialTheme.typography.bodyMedium.lineHeight.toDp()
                                }
                            )
                        )
                    }
                }

                SequentialProgress(
                    modifier = Modifier.fillMaxWidth(),
                    list = state.stepperList,
                    previewCountRight = 2,
                    previewCountLeft = 2,
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { }) {
                        Text("Previous")
                    }
                    Button(onClick = {
                        if (state.isPlaying) {
                            state = state.copy(isPlaying = false)
                        } else {
                            state = state.copy(isPlaying = true)
                        }
                    }) {
                        Text(if (state.isPlaying) "Pause" else "Play")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                }
            }
        }
    }
}