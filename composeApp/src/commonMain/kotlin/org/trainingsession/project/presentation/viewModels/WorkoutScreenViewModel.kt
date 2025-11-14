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

    fun playPause() {
        if (state.value.progressState.isPlaying) {
            progressController.pauseTimer()
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