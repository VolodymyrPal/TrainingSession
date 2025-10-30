package org.trainingsession.project.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoutes {
    @Serializable
    object ProgramListScreen : AppRoutes

    @Serializable
    data class ChosenProgramScreen (
        val program: Int
    ) : AppRoutes
}