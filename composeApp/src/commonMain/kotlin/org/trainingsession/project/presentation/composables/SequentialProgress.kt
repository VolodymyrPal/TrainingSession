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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
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
import org.trainingsession.project.presentation.theme.AppTheme
import org.trainingsession.project.presentation.viewModels.AppStepper
import kotlin.time.ExperimentalTime

interface Stepper {
    val durationMS: Long
    val elapsedTime: State<Long>
}

@Stable
class SequentialProgressState<T : Stepper>(val steps: List<T>, initialStepIndex: Int = 0) {

    companion object {
        fun <T : Stepper> Saver(steps: List<T>): Saver<SequentialProgressState<T>, Any> {
            return listSaver(
                save = { state ->
                    listOf(
                        state.currentStepIndex,
                        state.isPlaying,
                        state._stepStates.map { it.progress.value }.toFloatArray(),
                        state._stepStates.map { it.elapsedTime.value }.toLongArray()
                    )
                },
                restore = {
                    @Suppress("UNCHECKED_CAST")
                    val index = it[0] as Int
                    val isPlaying = it[1] as Boolean
                    val progresses = it[2] as FloatArray
                    val elapsedTimes = it[3] as LongArray

                    SequentialProgressState(steps, index).apply {
                        _isPlaying.value = isPlaying
                        progresses.forEachIndexed { i, progress ->
                            _stepStates.getOrNull(i)?.progress?.value = progress
                        }
                        elapsedTimes.forEachIndexed { i, time ->
                            _stepStates.getOrNull(i)?.elapsedTime?.value = time
                        }
                    }
                }
            )
        }
    }

    private val _currentStepIndex = mutableStateOf(
        initialStepIndex.coerceIn(
            0,
            steps.lastIndex.coerceAtLeast(0)
        )
    )
    val currentStepIndex: Int get() = _currentStepIndex.value

    private val _isPlaying = mutableStateOf(false)
    val isPlaying: Boolean get() = _isPlaying.value

    private val _stepStates = steps.map { PerStepState() }

    fun getProgress(index: Int): Float = _stepStates.getOrNull(index)?.progress?.value ?: 0f
    fun getElapsedTime(index: Int): Long = _stepStates.getOrNull(index)?.elapsedTime?.value ?: 0L
    fun isStepCompleted(index: Int): Boolean = getProgress(index) >= 1f

    val currentStepData: T? get() = steps.getOrNull(_currentStepIndex.value)
    val currentStepProgress: Float get() = getProgress(_currentStepIndex.value)
    val currentStepElapsedTime: Long get() = getElapsedTime(_currentStepIndex.value)

    internal fun setProgress(index: Int, progress: Float) {
        _stepStates.getOrNull(index)?.progress?.value = progress.coerceIn(0f, 1f)
    }

    internal fun setElapsedTime(index: Int, time: Long) {
        val duration = steps.getOrNull(index)?.durationMS ?: 0L
        _stepStates.getOrNull(index)?.elapsedTime?.value =
            time.coerceAtLeast(0L).coerceAtMost(duration)
    }

    val hasNextStep: Boolean get() = _currentStepIndex.value < steps.size - 1
    val hasPreviousStep: Boolean get() = _currentStepIndex.value > 0
    val allStepsCompleted: Boolean get() = _stepStates.all { it.progress.value == 1f } // Обновлено
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
        _stepStates.forEach { it.reset() }
    }

    fun resetCurrentStep() {
        _isPlaying.value = false
        _stepStates.getOrNull(_currentStepIndex.value)?.reset()
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
        setProgress(currentStepIndex, 1f)
        if (hasNextStep) {
            next()
        } else {
            _isPlaying.value = false
        }
    }

    fun playPause() {
        if (_isPlaying.value) {
            pause()
        } else {
            play()
        }
    }

    @Stable
    private class PerStepState(
        initialProgress: Float = 0f,
        initialTime: Long = 0L
    ) {
        val progress = mutableStateOf(initialProgress)
        val elapsedTime = mutableStateOf(initialTime)

        fun reset() {
            progress.value = 0f
            elapsedTime.value = 0L
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun SequentialProgress(
    list: List<AppStepper> = emptyList(),
    currentStepIndex: Int = 0,
    previewCountRight: Int = 2,
    previewCountLeft: Int = 1,
    modifier: Modifier = Modifier,
) {
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
                modifier = Modifier.padding(4.dp).weight(1f)
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
    Canvas(modifier.height(lineHeight).width(IntrinsicSize.Max)) {
        val progressWidth = size.width * progress().coerceIn(0f, 1f)

        drawLine(
            color = surfaceColor,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = size.height,
            cap = StrokeCap.Round
        )
        val safeProgress = progress().coerceIn(0f, 1f)
        val startFraction = 0.2f
        val endFraction = 0.7f
        val scaledFraction = startFraction + (endFraction - startFraction) * safeProgress

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
    lineWidth: Dp = 2.dp,
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

@Composable
fun <T : Stepper> rememberSequentialProgressState(
    steps: List<T>,
    initialStepIndex: Int = 0
): SequentialProgressState<T> {
    return rememberSaveable(steps, saver = SequentialProgressState.Saver(steps)) {
        SequentialProgressState(steps, initialStepIndex)
    }
}

data class ExerciseStep(
    val name: String,
    override val durationMS: Long,
) : Stepper

@Preview
@Composable
fun ProgressPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            val steps = remember {
                listOf(
                    ExerciseStep("Warm up", 5000L),
                    ExerciseStep("Push ups", 3000L),
                    ExerciseStep("Rest", 1000L),
                    ExerciseStep("Squats", 3000L),
                    ExerciseStep("Rest", 1000L),
                    ExerciseStep("Plank", 2000L),
                    ExerciseStep("Cool down", 2000L)
                )
            }

            val state = rememberSequentialProgressState(steps = steps)

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
                    val currentStep = state.currentStepData
                    if (currentStep != null) {
                        Text(
                            text = "${currentStep.name} (${state.currentStepIndex + 1}/${state.totalSteps})",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "${state.currentStepElapsedTime}ms / ${currentStep.durationMS}ms (${(state.currentStepProgress * 100).toInt()}%)",
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
                    state = state,
                    previewCountRight = 2,
                    previewCountLeft = 2,
                    onStepStart = { step, index ->
                        println("Started: ${step.name} (index: $index)")
                    },
                    onStepComplete = { step, index ->
                        println("Completed: ${step.name} (index: $index)")
                    },
                    onStepChange = { step, index ->
                        println("Changed to: ${step?.name} (index: $index)")
                    },
                    onProgressUpdate = { step, index, progress ->
                        // println("Progress: ${step.name} - $progress")
                    },
                    onAllStepsComplete = {
                        println("All steps completed!")
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { state.previous() }) {
                        Text("Previous")
                    }
                    Button(onClick = {
                        if (state.isPlaying) state.pause() else state.play()
                    }) {
                        Text(if (state.isPlaying) "Pause" else "Play")
                    }
                    Button(onClick = { state.stop() }) {
                        Text("Stop")
                    }
                    Button(onClick = { state.next() }) {
                        Text("Next")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { state.resetCurrentStep() }) {
                        Text("Reset Current")
                    }
                    Button(onClick = { state.seekToStep(3) }) {
                        Text("Go to Step 4")
                    }
                }
            }
        }
    }
}