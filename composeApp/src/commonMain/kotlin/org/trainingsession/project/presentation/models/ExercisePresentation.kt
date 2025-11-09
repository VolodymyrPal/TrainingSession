package org.trainingsession.project.presentation.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf

data class ExercisePresentation(
    val name: String,
    val durationSeconds: Int,
    val description: String,
) : Stepper {
    override val durationMS: Long = durationSeconds * 1000L
    override val progress: MutableState<Float> = mutableFloatStateOf(0f)
    override val elapsedTime: MutableState<Long> = mutableLongStateOf(0)
}