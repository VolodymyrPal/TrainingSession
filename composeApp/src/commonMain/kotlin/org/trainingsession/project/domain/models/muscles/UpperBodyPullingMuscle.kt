package org.trainingsession.project.domain.models.muscles

sealed interface UpperBodyPullingMuscle : MuscleGroupCategory {

    val categoryName: String
        get() = "Upper Body Pulling Muscle"

    data class BACK_LATS(
        override val name: String = "Latissimus Dorsi" // Широчайшие мышцы спины (Lats)
    ) : UpperBodyPullingMuscle

    data class BACK_UPPER(
        override val name: String = "Upper/Mid Back" // Общее название для Ромбовидных и Трапециевидных
    ) : UpperBodyPullingMuscle

    data class SPINAL_ERECTORS(
        override val name: String = "Spinal Erectors"
    ) : UpperBodyPullingMuscle // Разгибатели спины (Поясница)

    data class DELTOIDS_POSTERIOR(
        override val name: String = "Posterior Deltoids"
    ) : UpperBodyPullingMuscle // Дельтовидные (Задний пучок)

    data class BICEPS(
        override val name: String = "Biceps"
    ) : UpperBodyPullingMuscle

    data class FOREARMS(
        override val name: String = "Forearms"
    ) : UpperBodyPullingMuscle // Мышцы предплечья
}