package org.trainingsession.project.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoutes {
    @Serializable
    object ProgramListScreen : AppRoutes

    @Serializable
    object ChosenProgramScreen : AppRoutes
}