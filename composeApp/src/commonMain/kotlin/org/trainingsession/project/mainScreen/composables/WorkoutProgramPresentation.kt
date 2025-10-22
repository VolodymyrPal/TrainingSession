package org.trainingsession.project.mainScreen.composables

data class WorkoutProgramPresentation(
    val id: Int,
    val name: String,
    val description: String,
    val exercisePresentations: List<ExercisePresentation>
)