package org.trainingsession.project.domain.models.muscles

sealed interface AccessoryMuscle : MuscleGroup {

    val categoryName: String
        get() = "Accessory Muscle & Stabilizers"

    object ROTATOR_CUFF : AccessoryMuscle {
        override val name: String = "Rotator Cuff"
    } // Вращательная манжета плеча (для стабилизации)

    object NECK : AccessoryMuscle {
        override val name: String = "Neck"
    } // Мышцы шеи

    object FOREARMS : AccessoryMuscle {
        override val name: String = "Forearms (Flexors & Extensors)"
    } // Предплечья (сгибатели и разгибатели)

    object GRIP_MUSCLES : AccessoryMuscle {
        override val name: String = "Hand & Grip Muscles"
    } // Мелкие мышцы кисти для силы хвата

}