package org.trainingsession.project.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import org.trainingsession.project.domain.models.Program
import org.trainingsession.project.domain.repository.ProgramRepository
import org.trainingsession.project.presentation.models.ExercisePresentation
import org.trainingsession.project.presentation.models.Stepper
import org.trainingsession.project.presentation.models.toPresentation
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource


@KoinViewModel
class WorkoutScreenViewModel(
    private val repository: ProgramRepository,
    @InjectedParam
    val programId: Int
) : ViewModel() {

    private var timerJob: Job? = null
    private val timeSource = TimeSource.Monotonic

    private val _state = MutableStateFlow(ExerciseScreenState<ExercisePresentation>())
    val state: StateFlow<ExerciseScreenState<ExercisePresentation>> = _state.asStateFlow()


    init {
        fetchProgram(programId)
    }

    private fun fetchProgram(programId: Int) {
        viewModelScope.launch {
            val program = repository.getProgram(programId)

            val stepperList = program.toPresentation().exercisePresentations.map {
                it.copy(
                    durationSeconds = it.durationSeconds / 10
                )
            }


            _state.update { currentState ->
                currentState.copy(
                    programName = program.name,
                    chosenProgram = program,
                    stepperList = stepperList,
                    currentIndex = 0,
                    isPlaying = false,
                    isLoading = false
                )
            }
        }
    }

    private fun startTimer() {
        if (_state.value.isPlaying) return

        timerJob?.cancel()
        _state.update { it.copy(isPlaying = true) }

        timerJob = viewModelScope.launch {
            try {
                while (isActive && _state.value.isPlaying) {

                    val stateSnapshot = _state.value
                    val stepIndex = stateSnapshot.currentIndex
                    val currentStep = stateSnapshot.stepperList.getOrNull(stepIndex)

                    if (currentStep == null) {
                        pauseTimer()
                        break
                    }

                    val duration = currentStep.durationMS

                    if (duration <= 0L) {
                        currentStep.progress.value = 1f
                        onStepComplete()
                        continue
                    }

                    val elapsedBefore = currentStep.elapsedTime.value

                    if (elapsedBefore == 0L) {
                    } else {
                    }

                    val startTimeMark: TimeMark = timeSource.markNow() - elapsedBefore.milliseconds

                    while (isActive && _state.value.isPlaying && _state.value.currentIndex == stepIndex) {
                        val newElapsed = startTimeMark.elapsedNow().inWholeMilliseconds

                        if (newElapsed >= duration) {
                            currentStep.elapsedTime.value = duration
                            currentStep.progress.value = 1f

                            onStepComplete()
                        } else {
                            currentStep.elapsedTime.value = newElapsed
                            val progress = newElapsed.toFloat() / duration.toFloat()
                            currentStep.progress.value = progress
                        }

                        delay(16L)
                    }
                }
            } catch (e: CancellationException) {
            } catch (e: Exception) {
                pauseTimer()
            }
        }
    }

    sealed class SalesPageEvent {
        object PlayPause : SalesPageEvent()
        object NextStep : SalesPageEvent()
        object PreviousStep : SalesPageEvent()
        object ResetWorkout : SalesPageEvent()
        object ResetCurrentStep : SalesPageEvent()
    }

    fun event(event: WorkoutScreenViewModel.SalesPageEvent) {
        when (event) {
            SalesPageEvent.NextStep -> nextStep()
            SalesPageEvent.PlayPause -> playPause()
            SalesPageEvent.PreviousStep -> previousStep()
            SalesPageEvent.ResetCurrentStep -> resetCurrentStep()
            SalesPageEvent.ResetWorkout -> resetWorkout()
        }
    }
        } else {
            progressController.startTimer()
        }
    }

    fun nextStep() {
        progressController.pauseTimer()
        state.value.progressState.next()
    }

    fun previousStep() {
        progressController.pauseTimer()
        state.value.progressState.previous()
    }

    fun resetWorkout() {
        progressController.stopTimer()
    }

    fun resetCurrentStep() {
        progressController.resetCurrentStep()
    }
}

class AppStepper(
    override val durationMS: Long = 10L,
) : Stepper {
    override val elapsedTime: MutableState<Long> = mutableLongStateOf(0)
    val progress: MutableState<Float> = mutableFloatStateOf(0f)
}

data class ExerciseScreenState<T: Stepper>(
    val programName: String = "",
    val chosenProgram: Program = Program(
        id = 0,
        description = "",
        name = "",
        exercises = emptyList()
    ),
    val currentIndex: Int = 0,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = true,
    val stepperList: List<T> = emptyList()
)