package org.trainingsession.project.domain.models.muscles

sealed interface UpperBodyPullingMuscle : MuscleGroup {

    val categoryName: String
        get() = "Upper Body Pulling Muscle"

    object BACK_LATS : UpperBodyPullingMuscle {
        override val name: String = "Latissimus Dorsi"
    } // Широчайшие мышцы спины (Lats)

    object BACK_UPPER : UpperBodyPullingMuscle {
        override val name: String = "Upper/Mid Back"
    } // Общее название для Ромбовидных и Трапециевидных

    object SPINAL_ERECTORS : UpperBodyPullingMuscle {
        override val name: String = "Spinal Erectors"
    } // Разгибатели спины (Поясница)

    object DELTOIDS_POSTERIOR : UpperBodyPullingMuscle {
        override val name: String = "Posterior Deltoids"
    } // Дельтовидные (Задний пучок)

    object BICEPS : UpperBodyPullingMuscle {
        override val name: String = "Biceps"
    } // Бицепс

    object FOREARMS : UpperBodyPullingMuscle {
        override val name: String = "Forearms"
    } // Мышцы предплечья
}