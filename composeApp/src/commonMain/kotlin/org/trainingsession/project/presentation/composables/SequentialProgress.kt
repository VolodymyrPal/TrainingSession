package org.trainingsession.project.presentation.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.trainingsession.project.presentation.theme.AppTheme
import kotlin.time.ExperimentalTime

interface Stepper {
    val duration: Long
}

@OptIn(ExperimentalTime::class)
@Composable
fun <T : Stepper> SequentialProgressView(
    steps: List<T>,
    currentStepIndex: Int,
    isPlaying: Boolean,
    onStepComplete: () -> Unit = {},
    previewCountRight: Int = 2,
    previewCountLeft: Int = 1,
    modifier: Modifier = Modifier
) {
    var internalProgress by remember { mutableStateOf(0f) }
    var elapsedTime by remember(currentStepIndex) { mutableStateOf(0L) }
    val progressAnimatable = remember { Animatable(0f) }
    val progress: Float by progressAnimatable.asState()

    LaunchedEffect(currentStepIndex) {
        progressAnimatable.snapTo(0f)
        internalProgress = 0f
        elapsedTime = 0L
    }

    LaunchedEffect(currentStepIndex, isPlaying) {
        if (isPlaying && currentStepIndex < steps.size) {
            val currentStep = steps[currentStepIndex]
            val totalDuration = currentStep.duration
            val remainingTime = totalDuration - elapsedTime

            progressAnimatable.snapTo(internalProgress)

            progressAnimatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = remainingTime.toInt(),
                    easing = LinearEasing
                )
            ) {
                internalProgress = value
                elapsedTime = (totalDuration * value).toLong().coerceAtMost(totalDuration)
            }

            if (progressAnimatable.value >= 1f) {
                onStepComplete()
            }
        } else {
            progressAnimatable.stop()
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            (0 until currentStepIndex).forEach { index ->
                val toShow = index >= currentStepIndex - previewCountLeft

                key(index) {
                    ProgressDot(
                        isCompleted = true,
                        toShow = toShow,
                        modifier = Modifier.animateContentSize()
                    )
                }
            }
        }

        if (steps.isNotEmpty() && currentStepIndex < steps.size) { // Добавлена проверка, чтобы не рисовать коннектор, если все шаги пройдены
            ProgressConnector(
                progress = progress,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            (currentStepIndex until steps.size).forEach { index ->
                val isCurrent = index == currentStepIndex
                val isUpcomingCircle = index > currentStepIndex &&
                        index <= currentStepIndex + previewCountRight
                val toShow = isCurrent || isUpcomingCircle

                key(index) {
                    ProgressDot(
                        isCompleted = false,
                        toShow = toShow,
                        modifier = Modifier.animateContentSize()
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressConnector(
    progress: Float,
    modifier: Modifier = Modifier,
    lineHeight: Dp = 6.dp,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    surfaceColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    Box(
        modifier = modifier
            .height(lineHeight)
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val progressWidth = size.width * progress.coerceIn(0f, 1f)

            drawLine(
                color = surfaceColor,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                strokeWidth = size.height,
                cap = StrokeCap.Round
            )

            if (progressWidth > 0) {
                drawLine(
                    color = primaryColor,
                    start = Offset(0f, size.height / 2),
                    end = Offset(progressWidth, size.height / 2),
                    strokeWidth = size.height,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
fun ProgressDot(
    isCompleted: Boolean,
    toShow: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 10.dp,
    lineWidth: Dp = 4.dp,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    surfaceColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    val transition = updateTransition(targetState = toShow, label = "DotTransition")

    val animatedWidth by transition.animateDp(
        transitionSpec = { spring() }, label = "WidthAnimation"
    ) { isVisible -> if (isVisible) size else lineWidth }

    val animatedCornerRadius by transition.animateDp(
        transitionSpec = { spring() }, label = "CornerRadiusAnimation"
    ) { isVisible -> if (isVisible) size / 2 else 1.dp }

    Canvas(
        modifier.height(size).width(animatedWidth)
    ) {
        drawRoundRect(
            color = if (isCompleted) primaryColor else surfaceColor,
            size = Size(this.size.width, this.size.height),
            cornerRadius = CornerRadius(animatedCornerRadius.toPx())
        )
    }
}

data class ExerciseStep(
    val name: String,
    override val duration: Long
) : Stepper

@Preview
@Composable
fun ProgressPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            var currentStepIndex by remember { mutableStateOf(0) }
            var isPlaying by remember { mutableStateOf(true) }

            val steps = remember {
                listOf(
                    ExerciseStep("Warm up", 10000L),
                    ExerciseStep("Push ups", 3000L),
                    ExerciseStep("Rest", 1000L),
                    ExerciseStep("Squats", 3000L),
                    ExerciseStep("Rest", 1000L),
                    ExerciseStep("Plank", 2000L),
                    ExerciseStep("Cool down", 2000L)
                )
            }

            Button(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = { isPlaying = !isPlaying },
                content = {
                    Text(text = if (isPlaying) "Pause" else "Play")
                }
            )



            SequentialProgressView(
                modifier = Modifier.align(Alignment.Center),
                steps = steps,
                currentStepIndex = currentStepIndex,
                isPlaying = isPlaying,
                onStepComplete = {
                    currentStepIndex = (currentStepIndex + 1) % steps.size
                },
                previewCountRight = 2,
                previewCountLeft = 2
            )
        }
    }
}
