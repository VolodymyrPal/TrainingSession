package org.trainingsession.project.presentation.models

import org.trainingsession.project.domain.models.Program
import org.trainingsession.project.domain.models.Exercise

fun Exercise.toPresentation(): ExercisePresentation {
    val durationInSeconds = this.duration.inWholeSeconds.toInt()

    return ExercisePresentation(
        name = this.name,
        durationSeconds = durationInSeconds,
        description = this.description.value
    )
}

fun Program.toPresentation(): WorkoutProgramPresentation {
    return WorkoutProgramPresentation(
        id = this.id,
        name = this.name,
        description = this.description,
        exercisePresentations = this.exercises.map { it.toPresentation() }
    )
}

fun List<Program>.toPresentationList(): List<WorkoutProgramPresentation> {
    return this.map { it.toPresentation() }
}