package org.trainingsession.project.domain.models.muscles

sealed interface UpperBodyPullingMuscle : MuscleGroup {
    object BACK_LATS : UpperBodyPullingMuscle
    object TRAPEZIUS : UpperBodyPullingMuscle
    object RHOMBOIDS : UpperBodyPullingMuscle
    object SPINAL_ERECTORS : UpperBodyPullingMuscle
    object DELTOIDS_POSTERIOR : UpperBodyPullingMuscle
    object BICEPS : UpperBodyPullingMuscle
    object FOREARMS : UpperBodyPullingMuscle
}