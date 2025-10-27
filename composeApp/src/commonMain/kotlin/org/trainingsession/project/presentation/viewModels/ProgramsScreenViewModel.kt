package org.trainingsession.project.presentation.viewModels

import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel
import org.trainingsession.project.domain.models.Program
import org.trainingsession.project.domain.repository.ProgramRepository

@KoinViewModel
class ProgramsScreenViewModel(
    private val repository: ProgramRepository
) : ViewModel() {

    fun getPrograms(): List<Program> {
        return repository.getPrograms()
    }
}