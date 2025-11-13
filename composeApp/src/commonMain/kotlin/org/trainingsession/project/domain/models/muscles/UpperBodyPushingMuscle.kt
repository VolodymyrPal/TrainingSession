package org.trainingsession.project.domain.models.muscles

sealed interface UpperBodyPushingMuscle : MuscleGroup {

    val categoryName: String
        get() = "Upper Body Pushing Muscle"

    object CHEST : UpperBodyPushingMuscle {
        override val name: String = "Chest"
    } // Грудные мышцы (Большая и Малая)

    object DELTOIDS_ANTERIOR_MEDIAL : UpperBodyPushingMuscle {
        override val name: String = "Anterior & Medial Deltoids"
    } // Дельтовидные (Передний и Средний пучок)

    object TRICEPS : UpperBodyPushingMuscle {
        override val name: String = "Triceps"
    } // Трицепс

    object ROTATOR_CUFF : UpperBodyPushingMuscle {
        override val name: String = "Rotator Cuff"
    } // Вращательная манжета (Стабилизаторы плеча)

    object SERRATUS_ANTERIOR : UpperBodyPushingMuscle {
        override val name = "Serratus Anterior"
    } // Передняя зубчатая мышца

    object CHEST_UPPER : UpperBodyPushingMuscle {
        override val name = "Upper Chest"
    } // Верхняя часть груди
}