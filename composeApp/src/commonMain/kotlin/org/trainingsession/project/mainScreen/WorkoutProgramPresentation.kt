package org.trainingsession.project.mainScreen

import org.jetbrains.compose.resources.DrawableResource

data class WorkoutProgramPresentation(
    val id: Int,
    val name: String,
    val description: String,
    val exercisePresentations: List<ExercisePresentation>
)