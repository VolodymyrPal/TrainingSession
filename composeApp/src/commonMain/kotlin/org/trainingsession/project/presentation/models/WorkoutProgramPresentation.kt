package org.trainingsession.project.presentation.models

data class WorkoutProgramPresentation(
    val id: Int,
    val name: String,
    val description: String,
    val exercisePresentations: List<ExercisePresentation>
)