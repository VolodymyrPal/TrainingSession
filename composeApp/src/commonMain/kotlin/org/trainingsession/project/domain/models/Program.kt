package org.trainingsession.project.domain.models

data class Program(
    val id: Int,
    val name: String,
    val description: String,
    val exercisePresentations: List<Exercise>
)
