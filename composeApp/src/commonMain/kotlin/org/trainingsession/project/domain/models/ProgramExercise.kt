package org.trainingsession.project.domain.models

import kotlin.time.Duration

data class ProgramExercise(
    val id: Int,
    val name: String,
    val duration: Duration,
    val description: String,
    val primaryFocus: String = "For all body",
    val position: ExercisePosition = ExercisePosition.UNDEFINED,
    val instruction: ExerciseInstruction
)

data class ExerciseInstruction(
    val keyCues: String = "Be quite", // "Ключевые Инструкции" (например, "Плавно шагайте, соблюдая синхронизацию рук и ног.")
    val safetyFocus: SafetyFocus = SafetyFocus(), // Акцент на безопасности и технике
    val preemptiveCue: String? = "Try to breathe" // Проактивная подсказка для перехода (за 3-5 сек до конца)
)

data class SafetyFocus(
    val corePrinciple: String = "Be carefully",
    val modification: String? = "Careful with knees",
    val biomechanicalFocus: String = "Don't rush"
)

enum class ExercisePosition {
    STAY_UP, LAY_DOWN, ON_KNEES, UNDEFINED
}