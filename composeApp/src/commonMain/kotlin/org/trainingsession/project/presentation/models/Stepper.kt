package org.trainingsession.project.presentation.models

import androidx.compose.runtime.MutableState

interface Stepper {
    val progress: MutableState<Float>
    val durationMS: Long
    val elapsedTime: MutableState<Long>
}