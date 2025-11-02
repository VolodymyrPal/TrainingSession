package org.trainingsession.project.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import org.trainingsession.project.domain.models.Program
import org.trainingsession.project.domain.repository.ProgramRepository
import org.trainingsession.project.presentation.composables.SequentialProgressState
import org.trainingsession.project.presentation.composables.Stepper
import org.trainingsession.project.presentation.models.toPresentation
import org.trainingsession.project.utils.AppLogger

@KoinViewModel
class WorkoutScreenViewModel(
    private val repository: ProgramRepository,
    @InjectedParam
    val programId: Int
) : ViewModel() {

    private val _state = MutableStateFlow(ExerciseScreenState())
    val state: StateFlow<ExerciseScreenState> = _state.asStateFlow()


    init {
        fetchProgram(programId)
    }

    private fun fetchProgram(programId: Int) {
        viewModelScope.launch {
            val program = repository.getProgram(programId)
            AppLogger.d("program: ", "${state}")

            _state.update {
                it.copy(
                    chosenProgram = program
                )
            }

            val stepperList: List<AppStepper> = program.toPresentation().exercisePresentations.map { s ->
                object : AppStepper() {
                    override val durationMS: Long
                        get() = s.durationSeconds.toLong() * 15
                }
            }
            val newProgressState = SequentialProgressState(stepperList)
            _state.update { currentState ->
                currentState.copy(
                    programName = program.name,
                    chosenProgram = program,
                    progressState = newProgressState,
                )
            }
        }
    }
}

data class ExerciseScreenState(
    val programName: String = "",
    val chosenProgram: Program = Program(
        id = 0,
        description = "",
        name = "",
        programExercises = emptyList()
    ),
    val progressState: SequentialProgressState<AppStepper> = SequentialProgressState(emptyList())
)