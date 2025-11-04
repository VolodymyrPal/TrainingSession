package org.trainingsession.project.presentation.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.trainingsession.project.presentation.composables.SequentialProgressState
import org.trainingsession.project.presentation.composables.Stepper
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource


class ProgressController<T: Stepper> (
    private val state: SequentialProgressState<T>,
    private val scope: CoroutineScope
) {
    private var timerJob: Job? = null
    private val timeSource = TimeSource.Monotonic

    private val _events = MutableSharedFlow<ProgressEvent<T>>()
    val events: SharedFlow<ProgressEvent<T>> = _events.asSharedFlow()

    fun startTimer() {
        if (state.isPlaying) return

        timerJob?.cancel()
        state.play()

        timerJob = scope.launch {
            try {
                while (isActive && state.isPlaying) {
                    val stepIndex = state.currentStepIndex
                    val currentStep = state.currentStepData ?: break

                    val duration = currentStep.durationMS.takeIf { it > 0 } ?: run {
                        state.setProgress(stepIndex, 1f)
                        _events.emit(ProgressEvent.ProgressUpdate(currentStep, stepIndex, 1f))
                        state.onStepComplete()
                        _events.emit(ProgressEvent.StepComplete(currentStep, stepIndex))
                        if (state.allStepsCompleted) {
                            _events.emit(ProgressEvent.AllStepsComplete())
                        }
                        break
                    }

                    val elapsedBefore = state.getElapsedTime(stepIndex)

                    if (elapsedBefore == 0L) {
                        _events.emit(ProgressEvent.StepStart(currentStep, stepIndex))
                    } else {
                        _events.emit(ProgressEvent.StepContinue(currentStep, stepIndex))
                    }

                    val startTimeMark: TimeMark = timeSource.markNow() - elapsedBefore.milliseconds

                    while (isActive && state.isPlaying && state.currentStepIndex == stepIndex) {
                        val newElapsed = startTimeMark.elapsedNow().inWholeMilliseconds

                        if (newElapsed >= duration) {
                            state.setElapsedTime(stepIndex, duration)
                            state.setProgress(stepIndex, 1f)
                            _events.emit(ProgressEvent.ProgressUpdate(currentStep, stepIndex, 1f))

                            state.onStepComplete()
                            _events.emit(ProgressEvent.StepComplete(currentStep, stepIndex))

                            if (state.allStepsCompleted) {
                                _events.emit(ProgressEvent.AllStepsComplete())
                            }
//                            break
                        } else {
                            state.setElapsedTime(stepIndex, newElapsed)
                            val progress = newElapsed.toFloat() / duration.toFloat()
                            state.setProgress(stepIndex, progress)
                            _events.emit(ProgressEvent.ProgressUpdate(currentStep, stepIndex, progress))
                        }

                        delay(16L)
                    }
                }
            } catch (e: CancellationException) {
            } catch (e: Exception) {
                state.pause()
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        state.pause()
    }

    fun stopTimer() {
        timerJob?.cancel()
        state.stop()
    }

    fun resetTimer() {
        timerJob?.cancel()
        state.reset()
    }

    fun release() {
        timerJob?.cancel()
    }
}


sealed interface ProgressEvent<T : Stepper> {
    data class StepStart<T : Stepper>(val stepData: T, val index: Int) : ProgressEvent<T>
    data class StepContinue<T : Stepper>(val stepData: T, val index: Int) : ProgressEvent<T>
    data class StepComplete<T : Stepper>(val stepData: T, val index: Int) : ProgressEvent<T>
    data class ProgressUpdate<T : Stepper>(val stepData: T, val index: Int, val progress: Float) : ProgressEvent<T>
    class AllStepsComplete<T : Stepper> : ProgressEvent<T>
}