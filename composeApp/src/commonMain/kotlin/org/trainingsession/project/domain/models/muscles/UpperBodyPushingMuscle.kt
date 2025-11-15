package org.trainingsession.project.domain.models.muscles

sealed interface UpperBodyPushingMuscle : MuscleGroup {
    object CHEST : UpperBodyPushingMuscle
    object CHEST_UPPER : UpperBodyPushingMuscle
    object DELTOIDS_ANTERIOR : UpperBodyPushingMuscle
    object DELTOIDS_MEDIAL : UpperBodyPushingMuscle
    object TRICEPS : UpperBodyPushingMuscle
    object SERRATUS_ANTERIOR : UpperBodyPushingMuscle
}