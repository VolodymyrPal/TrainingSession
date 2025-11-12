package org.trainingsession.project.domain.models.muscles

sealed interface UpperBodyPushingMuscle : MuscleGroupCategory {

    val categoryName: String
        get() = "Upper Body Pushing Muscle"

    data class CHEST(
        override val name: String = "Chest"
    ) : UpperBodyPushingMuscle // Грудные мышцы (Большая и Малая)

    data class DELTOIDS_ANTERIOR_MEDIAL(
        override val name: String = "Anterior & Medial Deltoids"
    ) : UpperBodyPushingMuscle // Дельтовидные (Передний и Средний пучок)

    data class TRICEPS(
        override val name: String = "Triceps"
    ) : UpperBodyPushingMuscle // Трицепс

    data class ROTATOR_CUFF(
        override val name: String = "Rotator Cuff"
    ) : UpperBodyPushingMuscle // Вращательная манжета (Стабилизаторы плеча)
}