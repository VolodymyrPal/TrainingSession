package org.trainingsession.project.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.trainingsession.project.domain.models.Program
import org.trainingsession.project.domain.repository.ProgramRepository
import org.trainingsession.project.utils.AppLogger

@KoinViewModel
class ProgramsScreenViewModel(
    private val repository: ProgramRepository,
    private val httpClient: HttpClient
) : ViewModel() {

    init {
        viewModelScope.launch {
            val response = httpClient.get("https://ktor.io/docs/")
            AppLogger.d("AppResponse: ", "Response: ${response.bodyAsText()}")
        }
    }

    fun getPrograms(): List<Program> {
        return repository.getPrograms()
    }
}