package org.trainingsession.project.domain.models.muscles

sealed interface LowerBodyMuscle : MuscleGroup {

    val categoryName: String
        get() = "Lower Body Muscle"

    object QUADRICEPS : LowerBodyMuscle {
        override val name: String = "Quadriceps"
    } // Передняя поверхность бедра

    object GLUTES : LowerBodyMuscle {
        override val name: String = "Glutes"
    } // Ягодичные мышцы (Большая, Средняя, Малая)

    object HAMSTRINGS : LowerBodyMuscle {
        override val name: String = "Hamstrings"
    }  // Задняя поверхность бедра

    object ADDUCTORS : LowerBodyMuscle {
        override val name: String = "Adductors"
    } // Приводящие мышцы (Внутреннее бедро)

    object GASTROCNEMIUS : LowerBodyMuscle {
        override val name: String = "Gastrocnemius"
    } // Икроножная мышца (Голень, работает при прямом колене)

    object SOLEUS : LowerBodyMuscle {
        override val name: String = "Soleus"
    } // Камбаловидная мышца (Голень, работает при согнутом колене)

    object TIBIALIS_ANTERIOR : LowerBodyMuscle {
        override val name = "Tibialis Anterior"
    } //Передняя большеберцовая мышца
}