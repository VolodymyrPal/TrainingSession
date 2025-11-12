package org.trainingsession.project.domain.models.muscles

sealed interface LowerBodyMuscle : MuscleGroupCategory {

    val categoryName: String
        get() = "Lower Body Muscle"

    data class QUADRICEPS(
        override val name: String = "Quadriceps"
    ) : LowerBodyMuscle // Передняя поверхность бедра

    data class GLUTES(
        override val name: String = "Glutes" // Или "Gluteal Muscles"
    ) : LowerBodyMuscle // Ягодичные мышцы (Большая, Средняя, Малая)

    data class HAMSTRINGS(
        override val name: String = "Hamstrings"
    ) : LowerBodyMuscle // Задняя поверхность бедра

    data class ADDUCTORS(
        override val name: String = "Adductors"
    ) : LowerBodyMuscle // Приводящие мышцы (Внутреннее бедро)

    data class GASTROCNEMIUS(
        override val name: String = "Gastrocnemius"
    ) : LowerBodyMuscle // Икроножная мышца (Голень, работает при прямом колене)

    data class SOLEUS(
        override val name: String = "Soleus"
    ) : LowerBodyMuscle // Камбаловидная мышца (Голень, работает при согнутом колене)
}