package org.trainingsession.project.domain.models.muscles

sealed interface CoreMuscle : MuscleGroupCategory {

    val categoryName: String
        get() = "Core Muscle"

    data class ABS_RECTUS(
        override val name: String = "Rectus Abdominis"
    ) : CoreMuscle // Прямая мышца живота

    data class ABS_OBLIQUES(
        override val name: String = "Obliques"
    ) : CoreMuscle        // Косые мышцы живота

    data class ABS_TRANSVERSE(
        override val name: String = "Transversus Abdominis"
    ) : CoreMuscle       // Поперечная мышца живота (глубокий стабилизатор)
}