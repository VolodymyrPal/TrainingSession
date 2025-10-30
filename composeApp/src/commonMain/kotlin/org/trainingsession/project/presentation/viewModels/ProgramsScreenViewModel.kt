package org.trainingsession.project.presentation.viewModels

import androidx.lifecycle.ViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel
import org.trainingsession.project.domain.models.Program
import org.trainingsession.project.domain.repository.ProgramRepository
import org.trainingsession.project.presentation.models.WorkoutProgramPresentation
import org.trainingsession.project.presentation.models.toPresentationList

@KoinViewModel
class ProgramsScreenViewModel(
    private val repository: ProgramRepository,
    private val httpClient: HttpClient
) : ViewModel() {

    private val _state = MutableStateFlow(ProgramScreenState())
    val state: StateFlow<ProgramScreenState> = _state.asStateFlow()

    init {
        val programs = getPrograms().toPresentationList()
        _state.update {
            it.copy(
                programs = programs
            )
        }
    }

    fun getPrograms(): List<Program> {
        return repository.getPrograms()
    }
}

data class ProgramScreenState(
    val programs: List<WorkoutProgramPresentation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)