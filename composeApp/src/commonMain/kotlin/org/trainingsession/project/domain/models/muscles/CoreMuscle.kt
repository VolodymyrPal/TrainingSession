package org.trainingsession.project.domain.models.muscles

sealed interface CoreMuscle : MuscleGroup {

    val categoryName: String
        get() = "Core Muscle"

    object ABS_RECTUS : CoreMuscle {
        override val name: String = "Rectus Abdominis"
    } // Прямая мышца живота

    object ABS_OBLIQUES : CoreMuscle {
        override val name: String = "Obliques"
    } // Косые мышцы живота

    object ABS_TRANSVERSE : CoreMuscle {
        override val name: String = "Transversus Abdominis"
    } // Поперечная мышца живота (глубокий стабилизатор)

    object QUADRATUS_LUMBORUM : CoreMuscle {
        override val name = "Quadratus Lumborum"
    } // Квадратная мышца поясницы

    object PELVIC_FLOOR : CoreMuscle {
        override val name = "Pelvic Floor Muscles"
    } // Мышцы тазового дна
}