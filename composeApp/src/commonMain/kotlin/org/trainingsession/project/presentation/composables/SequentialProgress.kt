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

@Stable
class SequentialProgressState<T : Stepper>(val steps: List<T>, initialStepIndex: Int = 0) {
    private var _currentStepIndex = mutableStateOf(
        initialStepIndex.coerceIn(
            0,
            steps.lastIndex.coerceAtLeast(0)
        )
    )
    val currentStepIndex: Int get() = _currentStepIndex.value

    private var _isPlaying = mutableStateOf(false)
    val isPlaying: Boolean get() = _isPlaying.value

    private val _stepProgresses = steps.map { mutableStateOf(0f) }
    private val _stepElapsedTimes = steps.map { mutableStateOf(0L) }

    fun getProgress(index: Int): Float = _stepProgresses.getOrNull(index)?.value ?: 0f
    fun getElapsedTime(index: Int): Long = _stepElapsedTimes.getOrNull(index)?.value ?: 0L

    val currentStepData: T? get() = steps.getOrNull(_currentStepIndex.value)
    val currentStepProgress: Float get() = getProgress(_currentStepIndex.value)
    val currentStepElapsedTime: Long get() = getElapsedTime(_currentStepIndex.value)

    internal fun setProgress(index: Int, progress: Float) {
        _stepProgresses.getOrNull(index)?.value = progress.coerceIn(0f, 1f)
    }

    internal fun setElapsedTime(index: Int, time: Long) {
        val duration = steps.getOrNull(index)?.durationMS ?: 0L
        _stepElapsedTimes.getOrNull(index)?.value = time.coerceAtLeast(0L).coerceAtMost(duration)
    }

    val hasNextStep: Boolean get() = _currentStepIndex.value < steps.size - 1
    val hasPreviousStep: Boolean get() = _currentStepIndex.value > 0
    val isCompleted: Boolean get() = _currentStepIndex.value >= steps.size - 1 && currentStepProgress == 1f
    val totalSteps: Int get() = steps.size

    fun play() {
        if (_currentStepIndex.value < steps.size) {
            _isPlaying.value = true
        }
    }

    fun pause() {
        _isPlaying.value = false
    }

    fun stop() {
        _isPlaying.value = false
        reset()
    }

    fun reset() {
        _isPlaying.value = false
        _currentStepIndex.value = 0
        _stepProgresses.forEach { it.value = 0f }
        _stepElapsedTimes.forEach { it.value = 0L }
    }

    fun resetCurrentStep() {
        _isPlaying.value = false
        setProgress(_currentStepIndex.value, 0f)
        setElapsedTime(_currentStepIndex.value, 0L)
    }

    fun next() {
        if (hasNextStep) {
            _currentStepIndex.value++
        }
    }

    fun previous() {
        if (hasPreviousStep) {
            _currentStepIndex.value--
        }
    }

    fun seekToStep(stepIndex: Int) {
        if (stepIndex in 0 until steps.size) {
            _currentStepIndex.value = stepIndex
            _isPlaying.value = false
        }
    }

    internal fun onStepComplete() {
        if (hasNextStep) {
            next()
        } else {
            _isPlaying.value = false
            setProgress(_currentStepIndex.value, 1f)
        }
    }
}

@Composable
fun <T : Stepper> SequentialProgressView(
    state: SequentialProgressState<T>,
    previewCountRight: Int = 2,
    previewCountLeft: Int = 1,
    modifier: Modifier = Modifier,
    onStepStart: ((T, Int) -> Unit)? = null,
    onStepComplete: ((T, Int) -> Unit)? = null,
    onStepChange: ((T?, Int) -> Unit)? = null,
    onProgressUpdate: ((T, Int, Float) -> Unit)? = null,
    onAllStepsComplete: (() -> Unit)? = null
) {

    LaunchedEffect(state.currentStepIndex) {
        val stepIndex = state.currentStepIndex
        val currentStep = state.currentStepData
        if (currentStep == null || currentStep.durationMS <= 0L) {
            if (currentStep != null) state.onStepComplete()
            return@LaunchedEffect
        }

        snapshotFlow { state.isPlaying }.collect { isPlaying ->
            if (isPlaying) {
                val duration = currentStep.durationMS
                onStepStart?.invoke(currentStep, stepIndex)
                var elapsedTime = state.getElapsedTime(stepIndex)
                val startTime = withFrameNanos { it } - (elapsedTime * 1_000_000)
                while (elapsedTime < duration && state.isPlaying && state.currentStepIndex == stepIndex) {
                    val frameTime = withFrameNanos { it }
                    val newElapsedTime = (frameTime - startTime) / 1_000_000
                    val progress = newElapsedTime.toFloat() / duration.toFloat()
                    state.setElapsedTime(stepIndex, newElapsedTime)
                    state.setProgress(stepIndex, progress)
                    elapsedTime = newElapsedTime
                    onProgressUpdate?.invoke(currentStep, stepIndex, progress)
                }

                if (state.currentStepIndex == stepIndex && state.currentStepElapsedTime >= duration) {
                    state.setProgress(stepIndex, 1f)
                    onStepComplete?.invoke(currentStep, stepIndex)
                    if (!state.hasNextStep) onAllStepsComplete?.invoke()
                    state.onStepComplete()
                }
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            (0 until state.currentStepIndex).forEach { index ->
                val toShow = index >= state.currentStepIndex - previewCountLeft
                val isStepComplete = state.getProgress(index) >= 1f

                key(index) {
                    ProgressDot(
                        isCompleted = isStepComplete,
                        toShow = toShow,
                        modifier = Modifier.animateContentSize()
                    )
                }
            }
        }

        if (state.totalSteps > 0 && state.currentStepIndex < state.totalSteps) {
            ProgressConnector(
                progress = state.currentStepProgress,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            (state.currentStepIndex until state.totalSteps).forEach { index ->
                val isCurrent = index == state.currentStepIndex
                val isUpcomingCircle = index > state.currentStepIndex &&
                        index <= state.currentStepIndex + previewCountRight
                val toShow = isCurrent || isUpcomingCircle
                val isStepComplete = state.getProgress(index) >= 1f

                key(index) {
                    ProgressDot(
                        isCompleted = isStepComplete,
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
